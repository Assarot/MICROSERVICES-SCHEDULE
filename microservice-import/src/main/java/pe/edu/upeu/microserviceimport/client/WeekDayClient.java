package pe.edu.upeu.microserviceimport.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import pe.edu.upeu.microserviceimport.dto.WeekDayDTO;

import java.util.List;

@FeignClient(name = "microservice-schedule", contextId = "weekDayClient")
public interface WeekDayClient {

    @GetMapping("/api/v1/week-days")
    List<WeekDayDTO> getAllWeekDays();
}