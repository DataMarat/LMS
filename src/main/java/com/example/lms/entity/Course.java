package com.example.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;


/**
 * Учебный курс.
 * <p>
 * Сущность описывает основные свойства учебного курса, включая
 * идентификационный код, наименование, текстовое описание, количественные
 * характеристики и связь с преподавателем.
 */
@Entity
@Table(name = "courses")
public class Course extends AbstractEntity {

    /**
     * Наименование курса.
     */
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * Текстовое описание курса.
     */
    @Column(name = "description", length = 2000)
    private String description;

    /**
     * Продолжительность курса в академических часах.
     */
    @Column(name = "duration")
    private Integer duration;

    /**
     * Дата начала курса.
     * <p>
     * Поле добавлено в соответствии с примерной моделью из задания.
     */
    @Column(name = "start_date")
    private LocalDate startDate;

    public Course() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() { return duration; }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
