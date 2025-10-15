package pe.edu.upeu.microservice_course_management.utils;

import lombok.Getter;

@Getter
public enum ErrorCatalog {

    COURSE_NOT_FOUND("ERR_COURSE_001", "Course Not Found."),
    INVALID_COURSE("ERR_COURSE_002", "Invalid Course Parameters."),
    GENERIC_ERROR("ERR_GEN_003", "An unexpected error occurred.");

    private final String code;
    private final String message;
    ErrorCatalog(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
