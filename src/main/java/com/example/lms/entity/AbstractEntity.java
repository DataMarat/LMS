package com.example.lms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Базовый класс для всех сущностей доменной модели.
 * Содержит идентификатор и поле с датой создания записи.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity {

    /**
     * Уникальный идентификатор сущности.
     * Генерируется на уровне базы данных как целое число.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Дата и время создания записи в базе данных.
     * Заполняется автоматически при вставке новой записи.
     */
//    @CreationTimestamp
//    @Column(name = "created_at", updatable = false, nullable = false)
//    private LocalDateTime createdAt;
}