package com.example.lms.dto;

import com.example.lms.entity.Enrollment;

/**
 * DTO ответа для записи на курс.
 */
public class EnrollmentResponse {

    private Long id;
    private Long studentId;
    private Long courseId;

    public EnrollmentResponse() {
    }

    public static EnrollmentResponse from(Enrollment e) {
        EnrollmentResponse dto = new EnrollmentResponse();
        dto.id = e.getId();
        dto.studentId = e.getStudent() != null ? e.getStudent().getId() : null;
        dto.courseId = e.getCourse() != null ? e.getCourse().getId() : null;
        return dto;
    }

    public Long getId() {
        return id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public Long getCourseId() {
        return courseId;
    }
}
