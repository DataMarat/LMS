package com.example.lms.controller;

import com.example.lms.dto.CourseResponse;
import com.example.lms.dto.UserResponse;
import com.example.lms.entity.Course;
import com.example.lms.entity.Enrollment;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.EnrollmentRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

/**
 * REST API для чтения курсов и получения производных представлений (например, студентов курса).
 */
@Tag(name = "Courses", description = "Manage courses")
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public CourseController(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    /**
     * Возвращает список курсов.
     */
    @GetMapping
    public List<CourseResponse> getAll() {
        return courseRepository.findAll().stream().map(CourseResponse::from).toList();
    }

    /**
     * Возвращает курс по id.
     *
     * @param id идентификатор курса
     * @return DTO курса
     * @throws EntityNotFoundException если курс не найден
     */
    @Operation(summary = "Return course by id", description = "Shows course info by its id.")
    @GetMapping("/{id}")
    public CourseResponse getById(@PathVariable Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + id));
        return CourseResponse.from(course);
    }

    /**
     * Возвращает список студентов, записанных на курс.
     *
     * @param courseId идентификатор курса
     * @return список студентов
     */
    @GetMapping("/{courseId}/students")
    public List<UserResponse> getStudents(@PathVariable Long courseId) {
        // Валидация существования курса — чтобы возвращать 404 вместо пустого списка при ошибочном id.
        courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + courseId));

        List<Enrollment> enrollments = enrollmentRepository.findAllByCourseId(courseId);
        return enrollments.stream()
                .map(Enrollment::getStudent)
                .map(UserResponse::from)
                .toList();
    }
}
