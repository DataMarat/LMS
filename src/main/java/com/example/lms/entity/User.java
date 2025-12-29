package com.example.lms.entity;

import com.example.lms.entity.enums.UserStatus;
import com.example.lms.entity.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Базовая сущность, описывающая пользователя системы.
 * Может использоваться как основа для студентов, преподавателей и других ролей.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_user_email", columnNames = "email")
        })
public class User extends AbstractEntity {

    /**
     * Имя пользователя.
     */
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    /**
     * Фамилия пользователя.
     */
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    /**
     * Электронная почта пользователя.
     * Используется как уникальный идентификатор для входа и коммуникации.
     */
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    /**
     * Роль пользователя в системе.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole role;

    /**
     * Статус пользователя в системе.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private UserStatus status;

}
