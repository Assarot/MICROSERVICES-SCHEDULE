package pe.edu.upeu.microservice_schedule.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microservice_schedule.domain.model.Schedule;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.AutoAssignRequest;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.AutoAssignResponse;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.CourseToAssign;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.entity.WeekDayEntity;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.out.persistence.repository.WeekDayJpaRepository;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutoAssignService {

    private final ScheduleService scheduleService;
    private final WeekDayJpaRepository weekDayRepository;

    public AutoAssignResponse autoAssign(AutoAssignRequest request) {
        log.info("========== STARTING OPTIMIZED MULTI-SLOT AUTO ASSIGNMENT ==========");
        List<AutoAssignResponse.AssignedResult> assigned = new ArrayList<>();
        List<Long> failed = new ArrayList<>();

        List<String> startTimes = request.getStartTimes();
        int duration = request.getDurationMinutes() != null ? request.getDurationMinutes() : 90;
        List<Long> weekDayIds = request.getWeekDayIds();

        // 1. Cache day names
        Map<Long, String> dayNamesCache = new HashMap<>();
        for (Long weekDayId : weekDayIds) {
            String dayName = "";
            try {
                WeekDayEntity dayEntity = weekDayRepository.findById(weekDayId).orElse(null);
                if (dayEntity != null && dayEntity.getName() != null) {
                    dayName = dayEntity.getName().toUpperCase();
                }
            } catch (Exception e) {
                log.warn("Could not retrieve weekday by ID {}: {}", weekDayId, e.getMessage());
            }
            dayNamesCache.put(weekDayId, dayName);
        }

        // 2. Pre-fetch existing schedules
        List<Schedule> allExisting = scheduleService.findAll();
        // Memory Maps for Space, Teacher and Group
        Map<String, List<Schedule>> memorySpaceSchedules = new HashMap<>();
        Map<String, List<Schedule>> memoryTeacherSchedules = new HashMap<>();
        Map<String, List<Schedule>> memoryGroupSchedules = new HashMap<>();

        for (Schedule s : allExisting) {
            String spaceKey = s.getIdAcademicSpace() + "-" + s.getIdWeekName();
            memorySpaceSchedules.computeIfAbsent(spaceKey, k -> new ArrayList<>()).add(s);
            // Note: Ideally, fetch CourseAssignment entity to hydrate Teacher & Group
            // for existing DB records. For this session, we'll track them dynamically 
            // for the new assignments being generated in this batch.
        }

        // 3. MRV Heuristic (Minimum Remaining Values)
        // Sort courses by difficulty:
        // - Highest priority first
        // - Highest hours required first
        // - Fewest candidate spaces first
        List<CourseToAssign> sortedCourses = new ArrayList<>(request.getCourses());
        sortedCourses.sort((c1, c2) -> {
            int p1 = c1.getPriority() != null ? c1.getPriority() : 0;
            int p2 = c2.getPriority() != null ? c2.getPriority() : 0;
            if (p1 != p2) return Integer.compare(p2, p1); // Descending priority

            int h1 = c1.getHoursRequired() != null ? c1.getHoursRequired() : 0;
            int h2 = c2.getHoursRequired() != null ? c2.getHoursRequired() : 0;
            if (h1 != h2) return Integer.compare(h2, h1); // Descending hours

            int s1 = c1.getCandidateAcademicSpaceIds() != null ? c1.getCandidateAcademicSpaceIds().size() : 999;
            int s2 = c2.getCandidateAcademicSpaceIds() != null ? c2.getCandidateAcademicSpaceIds().size() : 999;
            return Integer.compare(s1, s2); // Ascending candidate spaces
        });

        // 4. Constraint Solving Loop
        for (var course : sortedCourses) {
            if (course.getCandidateAcademicSpaceIds() == null || course.getCandidateAcademicSpaceIds().isEmpty()) {
                failed.add(course.getIdCourseAssignment());
                continue;
            }

            int slotsNeeded = course.getHoursRequired() != null 
                ? (int) Math.ceil((double) course.getHoursRequired() / ((double) duration / 60.0)) 
                : 1;

            log.info("Scheduling Course: {}, Slots Needed: {}", course.getIdCourseAssignment(), slotsNeeded);

            int slotsAssigned = 0;
            Set<Long> assignedDays = new HashSet<>();

            for (int pass = 1; pass <= 2; pass++) {
                if (slotsAssigned >= slotsNeeded) break;

                for (Long spaceId : course.getCandidateAcademicSpaceIds()) {
                    if (slotsAssigned >= slotsNeeded) break;

                    for (Long weekDayId : weekDayIds) {
                        if (slotsAssigned >= slotsNeeded) break;

                        // Pass 1: Spread across unique days
                        if (pass == 1 && assignedDays.contains(weekDayId)) continue;

                        String dayName = dayNamesCache.getOrDefault(weekDayId, "");
                        if (dayName.contains("SABADO") || dayName.contains("SÁBADO") || dayName.contains("DOMINGO")) {
                            continue;
                        }

                        for (String startTimeStr : startTimes) {
                            LocalTime start = LocalTime.parse(startTimeStr);
                            LocalTime end = start.plusMinutes(duration);

                            LocalTime minTime = LocalTime.of(7, 25);
                            LocalTime maxTime = LocalTime.of(22, 30);
                            if (start.isBefore(minTime) || end.isAfter(maxTime)) continue;

                            if (dayName.contains("VIERNES") || dayName.contains("VIER")) {
                                if (end.isAfter(LocalTime.of(13, 10))) continue;
                            }

                            // Conflict Checks
                            boolean conflict = false;

                            // Space Conflict
                            String spaceKey = spaceId + "-" + weekDayId;
                            for (Schedule s : memorySpaceSchedules.getOrDefault(spaceKey, Collections.emptyList())) {
                                if (start.isBefore(s.getEndTime()) && end.isAfter(s.getStartTime())) {
                                    conflict = true; break;
                                }
                            }

                            // Teacher Conflict
                            if (!conflict && course.getIdTeacher() != null) {
                                String teacherKey = course.getIdTeacher() + "-" + weekDayId;
                                for (Schedule s : memoryTeacherSchedules.getOrDefault(teacherKey, Collections.emptyList())) {
                                    if (start.isBefore(s.getEndTime()) && end.isAfter(s.getStartTime())) {
                                        conflict = true; break;
                                    }
                                }
                            }

                            // Group Conflict
                            if (!conflict && course.getIdGroup() != null) {
                                String groupKey = course.getIdGroup() + "-" + weekDayId;
                                for (Schedule s : memoryGroupSchedules.getOrDefault(groupKey, Collections.emptyList())) {
                                    if (start.isBefore(s.getEndTime()) && end.isAfter(s.getStartTime())) {
                                        conflict = true; break;
                                    }
                                }
                            }

                            // Anti-Gap Policy Check (Max 3 hours gap for students)
                            if (!conflict && course.getIdGroup() != null) {
                                String groupKey = course.getIdGroup() + "-" + weekDayId;
                                List<Schedule> groupScheds = new ArrayList<>(memoryGroupSchedules.getOrDefault(groupKey, Collections.emptyList()));
                                
                                // Fake a schedule for the new proposed time
                                Schedule proposed = Schedule.builder().startTime(start).endTime(end).build();
                                groupScheds.add(proposed);
                                
                                // Sort by start time
                                groupScheds.sort(Comparator.comparing(Schedule::getStartTime));
                                
                                // Check gaps only between adjacent classes
                                for (int i = 0; i < groupScheds.size() - 1; i++) {
                                    Schedule current = groupScheds.get(i);
                                    Schedule next = groupScheds.get(i + 1);
                                    long gapMinutes = java.time.Duration.between(current.getEndTime(), next.getStartTime()).toMinutes();
                                    if (gapMinutes > 180) {
                                        conflict = true;
                                        break;
                                    }
                                }
                            }

                            if (!conflict) {
                                Schedule schedule = Schedule.builder()
                                        .startTime(start)
                                        .endTime(end)
                                        .duration(duration)
                                        .idAcademicSpace(spaceId)
                                        .idCourseAssignment(course.getIdCourseAssignment())
                                        .idWeekName(weekDayId)
                                        .idTypeSchedule(course.getIdTypeSchedule() != null ? course.getIdTypeSchedule() : 1L)
                                        .build();

                                Schedule saved = scheduleService.create(schedule);
                                assigned.add(AutoAssignResponse.AssignedResult.builder()
                                        .idCourseAssignment(course.getIdCourseAssignment())
                                        .idAcademicSpace(spaceId)
                                        .startTime(start.toString())
                                        .endTime(end.toString())
                                        .weekDayId(weekDayId)
                                        .idSchedule(saved.getIdSchedule())
                                        .build());

                                slotsAssigned++;
                                assignedDays.add(weekDayId);

                                // Update memory caches
                                memorySpaceSchedules.computeIfAbsent(spaceKey, k -> new ArrayList<>()).add(saved);
                                if (course.getIdTeacher() != null) {
                                    String teacherKey = course.getIdTeacher() + "-" + weekDayId;
                                    memoryTeacherSchedules.computeIfAbsent(teacherKey, k -> new ArrayList<>()).add(saved);
                                }
                                if (course.getIdGroup() != null) {
                                    String groupKey = course.getIdGroup() + "-" + weekDayId;
                                    memoryGroupSchedules.computeIfAbsent(groupKey, k -> new ArrayList<>()).add(saved);
                                }

                                log.info("✓ Assigned: {} {} - {} | Space: {} | Teacher: {} | Group: {}", 
                                    dayName, start, end, spaceId, course.getIdTeacher(), course.getIdGroup());

                                if (slotsAssigned >= slotsNeeded) break;
                            }
                        }
                    }
                }
            }

            if (slotsAssigned == 0) {
                log.warn("✗ Failed to assign any slots for Course: {}", course.getIdCourseAssignment());
                failed.add(course.getIdCourseAssignment());
            } else if (slotsAssigned < slotsNeeded) {
                log.warn("! Partially assigned course {}: {}/{} slots", course.getIdCourseAssignment(), slotsAssigned, slotsNeeded);
            }
        }

        log.info("========== OPTIMIZED AUTO ASSIGNMENT ENDED. Assigned: {}, Failed: {} ==========", assigned.size(), failed.size());
        return AutoAssignResponse.builder().assigned(assigned).failedCourseAssignmentIds(failed).build();
    }
}