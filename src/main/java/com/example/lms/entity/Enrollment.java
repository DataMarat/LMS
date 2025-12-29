package com.example.lms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Запись студента на курс.
 * <p>
 * Промежуточная сущность для связи "многие-ко-многим" между User и Course,
 * которая позволяет хранить дополнительные поля (при необходимости).
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "enrollments",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_enrollment_student_course",
                columnNames = {"student_id", "course_id"}
        )
)
public class Enrollment extends AbstractEntity {

    /**
     * Студент, записанный на курс.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    /**
     * Курс, на который записан студент.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    public Enrollment(User student, Course course) {
        this.student = student;
        this.course = course;
    }
}
