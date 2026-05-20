package pe.edu.upeu.microservice_schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.microservice_schedule.domain.model.Schedule;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.AutoAssignRequest;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.AutoAssignResponse;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AutoAssignService {

    private final ScheduleService scheduleService;

    public AutoAssignResponse autoAssign(AutoAssignRequest request) {
        List<AutoAssignResponse.AssignedResult> assigned = new ArrayList<>();
        List<Long> failed = new ArrayList<>();

        List<String> startTimes = request.getStartTimes();
        int duration = request.getDurationMinutes() != null ? request.getDurationMinutes() : 90;
        List<Long> weekDayIds = request.getWeekDayIds();

        // For each course, try to assign to any candidate academic space and slot
        for (var course : request.getCourses()) {
            boolean ok = false;
            if (course.getCandidateAcademicSpaceIds() == null || course.getCandidateAcademicSpaceIds().isEmpty()) {
                failed.add(course.getIdCourseAssignment());
                continue;
            }

            for (Long spaceId : course.getCandidateAcademicSpaceIds()) {
                for (Long weekDayId : weekDayIds) {
                    for (String startTimeStr : startTimes) {
                        LocalTime start = LocalTime.parse(startTimeStr);
                        LocalTime end = start.plusMinutes(duration);

                        // check conflicts
                        boolean conflict = scheduleService.existsConflict(spaceId, weekDayId, start, end);
                        if (!conflict) {
                            Schedule schedule = Schedule.builder()
                                    .startTime(start)
                                    .endTime(end)
                                    .duration(duration)
                                    .idAcademicSpace(spaceId)
                                    .idCourseAssignment(course.getIdCourseAssignment())
                                    .idWeekName(weekDayId)
                                    .idTypeSchedule(1L)
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
                            ok = true;
                            break;
                        }
                    }
                    if (ok) break;
                }
                if (ok) break;
            }

            if (!ok) failed.add(course.getIdCourseAssignment());
        }

        return AutoAssignResponse.builder().assigned(assigned).failedCourseAssignmentIds(failed).build();
    }
}