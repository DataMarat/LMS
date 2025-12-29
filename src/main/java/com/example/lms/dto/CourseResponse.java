package com.example.lms.dto;

import com.example.lms.entity.Course;

/**
 * DTO ответа для курса.
 */
public class CourseResponse {

    private Long id;
    private String title;
    private String description;
    private Long teacherId;

    public CourseResponse() {
    }

    public static CourseResponse from(Course course) {
        CourseResponse dto = new CourseResponse();
        dto.id = course.getId();
        dto.title = course.getTitle();
        dto.description = course.getDescription();
//        dto.teacherId = course.getTeacher() != null ? course.getTeacher().getId() : null;
        return dto;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getTeacherId() {
        return teacherId;
    }
}
