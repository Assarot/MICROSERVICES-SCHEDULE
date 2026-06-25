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
            if (p1 != p2)
                return Integer.compare(p2, p1); // Descending priority

            int h1 = c1.getHoursRequired() != null ? c1.getHoursRequired() : 0;
            int h2 = c2.getHoursRequired() != null ? c2.getHoursRequired() : 0;
            if (h1 != h2)
                return Integer.compare(h2, h1); // Descending hours

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

            int slotsNeeded = course.getHoursRequired() != null ? course.getHoursRequired() : 1;

            log.info("Scheduling Course: {}, Slots Needed: {}", course.getIdCourseAssignment(), slotsNeeded);

            int slotsAssigned = 0;
            Set<Long> assignedDays = new HashSet<>();

            for (int pass = 1; pass <= 2; pass++) {
                if (slotsAssigned >= slotsNeeded)
                    break;

                for (Long spaceId : course.getCandidateAcademicSpaceIds()) {
                    if (slotsAssigned >= slotsNeeded)
                        break;

                    for (Long weekDayId : weekDayIds) {
                        if (slotsAssigned >= slotsNeeded)
                            break;

                        // Pass 1: Spread across unique days
                        if (pass == 1 && assignedDays.contains(weekDayId))
                            continue;

                        String dayName = dayNamesCache.getOrDefault(weekDayId, "");
                        if (dayName.contains("SABADO") || dayName.contains("SÁBADO") || dayName.contains("DOMINGO")) {
                            continue;
                        }

                        for (int i = 0; i < startTimes.size(); i++) {
                            String startTimeStr = startTimes.get(i);
                            LocalTime start = LocalTime.parse(startTimeStr);

                            // Determine exact end time by looking ahead in startTimes
                            // If slotsNeeded is 4, and start is at index i, the block ends at the end time
                            // of index i + slotsNeeded - 1.
                            // If we exceed the array, we default to chronological addition, but we
                            // shouldn't.
                            int endIndex = Math.min(i + slotsNeeded - 1, startTimes.size() - 1);
                            LocalTime exactEnd;

                            // Hardcoded end times for the 16 standard UPeU academic blocks
                            List<LocalTime> academicEndTimes = Arrays.asList(
                                    LocalTime.of(8, 20), // 07:30
                                    LocalTime.of(9, 15), // 08:25
                                    LocalTime.of(10, 10), // 09:20
                                    LocalTime.of(11, 10), // 10:20
                                    LocalTime.of(12, 5), // 11:15
                                    LocalTime.of(13, 0), // 12:10
                                    LocalTime.of(14, 0), // 13:10
                                    LocalTime.of(14, 55), // 14:05
                                    LocalTime.of(15, 50), // 15:00
                                    LocalTime.of(16, 50), // 16:00
                                    LocalTime.of(17, 45), // 16:55
                                    LocalTime.of(18, 40), // 17:50
                                    LocalTime.of(19, 35), // 18:45
                                    LocalTime.of(20, 30), // 19:40
                                    LocalTime.of(21, 25), // 20:35
                                    LocalTime.of(22, 20) // 21:30
                            );

                            // Try to match start time to find the real end time
                            boolean matchedSlot = false;
                            for (int sIdx = 0; sIdx < 16; sIdx++) {
                                // We approximate matching by checking if start matches standard start times
                                // For simplicity, we just use the relative index if the startTimes passed align
                                // Actually, startTimes is either Mañana, Tarde or Ambos.
                            }

                            // A foolproof way:
                            List<String> allStarts = Arrays.asList(
                                    "07:30:00", "08:25:00", "09:20:00", "10:20:00", "11:15:00", "12:10:00",
                                    "13:10:00", "14:05:00", "15:00:00", "16:00:00", "16:55:00", "17:50:00",
                                    "18:45:00", "19:40:00", "20:35:00", "21:30:00");

                            int globalStartIdx = allStarts.indexOf(startTimeStr);
                            if (globalStartIdx != -1) {
                                int globalEndIdx = Math.min(globalStartIdx + slotsNeeded - 1, allStarts.size() - 1);
                                exactEnd = academicEndTimes.get(globalEndIdx);
                            } else {
                                exactEnd = start.plusMinutes(duration * slotsNeeded);
                            }

                            int totalDurationForCourse = (int) java.time.Duration.between(start, exactEnd).toMinutes();
                            LocalTime end = exactEnd;

                            LocalTime minTime = LocalTime.of(7, 25);
                            LocalTime maxTime = LocalTime.of(22, 30);
                            if (start.isBefore(minTime) || end.isAfter(maxTime))
                                continue;

                            if (dayName.contains("VIERNES") || dayName.contains("VIER")) {
                                if (end.isAfter(LocalTime.of(13, 10)))
                                    continue;
                            }

                            // Lunch Boundary Check (13:00 to 13:10)
                            // "los cursos no deberían cruzar con la horara de almuerzo o la hora que dice TARDE que es de 1:00-1:10"
                            LocalTime lunchStart = LocalTime.of(13, 0);
                            LocalTime lunchEnd = LocalTime.of(13, 10);
                            if (start.isBefore(lunchEnd) && end.isAfter(lunchStart)) {
                                continue;
                            }

                            // Dynamic Cultura Check
                            if (request.getCulturaDayId() != null && request.getCulturaDayId().equals(weekDayId)) {
                                if (request.getCulturaStartTime() != null && request.getCulturaEndTime() != null) {
                                    LocalTime culturaStart = LocalTime.parse(request.getCulturaStartTime());
                                    LocalTime culturaEnd = LocalTime.parse(request.getCulturaEndTime());
                                    if (start.isBefore(culturaEnd) && end.isAfter(culturaStart)) {
                                        continue;
                                    }
                                }
                            }

                            // Dynamic Activate Check
                            if (request.getActivateDayId() != null && request.getActivateDayId().equals(weekDayId)) {
                                if (request.getActivateStartTime() != null && request.getActivateEndTime() != null) {
                                    LocalTime activateStart = LocalTime.parse(request.getActivateStartTime());
                                    LocalTime activateEnd = LocalTime.parse(request.getActivateEndTime());
                                    if (start.isBefore(activateEnd) && end.isAfter(activateStart)) {
                                        continue;
                                    }
                                }
                            }

                            // Conflict Checks
                            boolean conflict = false;

                            // Space Conflict
                            String spaceKey = spaceId + "-" + weekDayId;
                            for (Schedule s : memorySpaceSchedules.getOrDefault(spaceKey, Collections.emptyList())) {
                                if (start.isBefore(s.getEndTime()) && end.isAfter(s.getStartTime())) {
                                    conflict = true;
                                    break;
                                }
                            }

                            // Teacher Conflict
                            if (!conflict && course.getIdTeacher() != null) {
                                String teacherKey = course.getIdTeacher() + "-" + weekDayId;
                                for (Schedule s : memoryTeacherSchedules.getOrDefault(teacherKey,
                                        Collections.emptyList())) {
                                    if (start.isBefore(s.getEndTime()) && end.isAfter(s.getStartTime())) {
                                        conflict = true;
                                        break;
                                    }
                                }
                            }

                            // Group Conflict
                            if (!conflict && course.getIdGroup() != null) {
                                String groupKey = course.getIdGroup() + "-" + weekDayId;
                                for (Schedule s : memoryGroupSchedules.getOrDefault(groupKey,
                                        Collections.emptyList())) {
                                    if (start.isBefore(s.getEndTime()) && end.isAfter(s.getStartTime())) {
                                        conflict = true;
                                        break;
                                    }
                                }
                            }

                            // Anti-Gap Policy Check (Max 3 hours gap for students)
                            if (!conflict && course.getIdGroup() != null) {
                                String groupKey = course.getIdGroup() + "-" + weekDayId;
                                List<Schedule> groupScheds = new ArrayList<>(
                                        memoryGroupSchedules.getOrDefault(groupKey, Collections.emptyList()));

                                // Fake a schedule for the new proposed time
                                Schedule proposed = Schedule.builder().startTime(start).endTime(end).build();
                                groupScheds.add(proposed);

                                // Sort by start time
                                groupScheds.sort(Comparator.comparing(Schedule::getStartTime));

                                // Check gaps only between adjacent classes
                                for (int j = 0; j < groupScheds.size() - 1; j++) {
                                    Schedule current = groupScheds.get(j);
                                    Schedule next = groupScheds.get(j + 1);
                                    long gapMinutes = java.time.Duration
                                            .between(current.getEndTime(), next.getStartTime()).toMinutes();
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
                                        .duration(totalDurationForCourse)
                                        .idAcademicSpace(spaceId)
                                        .idCourseAssignment(course.getIdCourseAssignment())
                                        .idWeekName(weekDayId)
                                        .idTypeSchedule(
                                                course.getIdTypeSchedule() != null ? course.getIdTypeSchedule() : 1L)
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

                                slotsAssigned += slotsNeeded;
                                assignedDays.add(weekDayId);

                                // Update memory caches
                                memorySpaceSchedules.computeIfAbsent(spaceKey, k -> new ArrayList<>()).add(saved);
                                if (course.getIdTeacher() != null) {
                                    String teacherKey = course.getIdTeacher() + "-" + weekDayId;
                                    memoryTeacherSchedules.computeIfAbsent(teacherKey, k -> new ArrayList<>())
                                            .add(saved);
                                }
                                if (course.getIdGroup() != null) {
                                    String groupKey = course.getIdGroup() + "-" + weekDayId;
                                    memoryGroupSchedules.computeIfAbsent(groupKey, k -> new ArrayList<>()).add(saved);
                                }

                                log.info("✓ Assigned: {} {} - {} | Space: {} | Teacher: {} | Group: {}",
                                        dayName, start, end, spaceId, course.getIdTeacher(), course.getIdGroup());

                                if (slotsAssigned >= slotsNeeded)
                                    break;
                            }
                        }
                    }
                }
            }

            if (slotsAssigned == 0) {
                log.warn("✗ Failed to assign any slots for Course: {}", course.getIdCourseAssignment());
                failed.add(course.getIdCourseAssignment());
            } else if (slotsAssigned < slotsNeeded) {
                log.warn("! Partially assigned course {}: {}/{} slots", course.getIdCourseAssignment(), slotsAssigned,
                        slotsNeeded);
            }
        }

        // 5. Global Institutional Blocks Saving
        saveGlobalInstitutionalBlockIfMissing(request.getCulturaDayId(), request.getCulturaStartTime(), request.getCulturaEndTime(), 101L, allExisting);
        saveGlobalInstitutionalBlockIfMissing(request.getActivateDayId(), request.getActivateStartTime(), request.getActivateEndTime(), 102L, allExisting);

        log.info("========== OPTIMIZED AUTO ASSIGNMENT ENDED. Assigned: {}, Failed: {} ==========", assigned.size(),
                failed.size());
        return AutoAssignResponse.builder().assigned(assigned).failedCourseAssignmentIds(failed).build();
    }

    private void saveGlobalInstitutionalBlockIfMissing(Long dayId, String startTime, String endTime, Long typeId, List<Schedule> allExisting) {
        if (dayId != null && startTime != null && endTime != null) {
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            boolean exists = allExisting.stream()
                .anyMatch(s -> s.getIdTypeSchedule() != null && s.getIdTypeSchedule().equals(typeId) &&
                               s.getIdWeekName() != null && s.getIdWeekName().equals(dayId) &&
                               s.getStartTime() != null && s.getStartTime().equals(start) &&
                               s.getEndTime() != null && s.getEndTime().equals(end));
            
            if (!exists) {
                log.info("Creating Global Institutional Block Type {} for Day {}, {} - {}", typeId, dayId, start, end);
                int duration = (int) java.time.Duration.between(start, end).toMinutes();
                scheduleService.create(Schedule.builder()
                        .startTime(start)
                        .endTime(end)
                        .duration(duration)
                        .idTypeSchedule(typeId)
                        .idWeekName(dayId)
                        .build());
            }
        }
    }
}