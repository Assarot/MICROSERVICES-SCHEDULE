package pe.edu.upeu.microserviceimport.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.upeu.microserviceimport.dto.CreateScheduleDTO;

import java.util.List;

@FeignClient(name = "microservice-schedule", contextId = "scheduleClient")
public interface ScheduleClient {

    @PostMapping("/api/v1/schedules")
    CreateScheduleDTO createSchedule(@RequestBody CreateScheduleDTO scheduleDTO);

    @GetMapping("/api/v1/schedules/{id}")
    CreateScheduleDTO getScheduleById(@PathVariable("id") Long id);

    @GetMapping("/api/v1/schedules")
    List<CreateScheduleDTO> getAllSchedules();
}
