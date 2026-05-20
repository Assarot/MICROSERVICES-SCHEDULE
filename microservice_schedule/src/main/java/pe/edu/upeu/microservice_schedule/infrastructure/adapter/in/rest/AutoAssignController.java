package pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upeu.microservice_schedule.application.service.AutoAssignService;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.AutoAssignRequest;
import pe.edu.upeu.microservice_schedule.infrastructure.adapter.in.rest.dto.AutoAssignResponse;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class AutoAssignController {

    private final AutoAssignService autoAssignService;

    @PostMapping("/auto-assign")
    public ResponseEntity<AutoAssignResponse> autoAssign(@RequestBody AutoAssignRequest request) {
        var resp = autoAssignService.autoAssign(request);
        return ResponseEntity.ok(resp);
    }
}
