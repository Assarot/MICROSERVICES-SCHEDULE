package pe.edu.upeu.microservice_reservation.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.upeu.microservice_reservation.infrastructure.client.dto.CourseClientDto;

/**
 * Feign client para comunicarse con MS-COURSE-MANAGEMENT.
 * Nombre Eureka: ms-course-management (puerto 8083)
 * Endpoint del course: /course/v1/api/{id}
 */
@FeignClient(name = "ms-course-management")
public interface CourseClient {

    @GetMapping("/course/v1/api/{id}")
    CourseClientDto getCourseById(@PathVariable("id") Long id);
}
