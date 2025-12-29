package com.example.lms.controller;

import com.example.lms.dto.EnrollmentRequest;
import com.example.lms.entity.Enrollment;
import com.example.lms.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

/**
 * REST API для управления записями студентов на курсы.
 */
@Tag(name = "Enrollments", description = "Manage student enrollments")
@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    /**
     * Записывает студента на курс.
     *
     * @param request запрос с идентификаторами студента и курса
     * @return созданная запись Enrollment
     */
    @Operation(summary = "Enroll student to course", description = "Creates an enrollment record.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Enrollment enroll(@Valid @RequestBody EnrollmentRequest request) {
        return enrollmentService.enroll(request.getStudentId(), request.getCourseId());
    }

    /**
     * Отписывает студента от курса.
     *
     * @param request запрос с идентификаторами студента и курса
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unenroll(@Valid @RequestBody EnrollmentRequest request) {
        enrollmentService.unenroll(request.getStudentId(), request.getCourseId());
    }
}
