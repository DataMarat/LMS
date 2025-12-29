package com.example.lms.dto;

import com.example.lms.entity.User;

/**
 * DTO ответа для пользователя.
 * Используется для защиты от утечек JPA-сущностей наружу и для контроля формата API.
 */
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String status;
    private String role;

    public UserResponse() {
    }

    public static UserResponse from(User user) {
        UserResponse dto = new UserResponse();
        dto.id = user.getId();
        dto.firstName = user.getFirstName();
        dto.lastName = user.getLastName();
        dto.email = user.getEmail();
        dto.status = user.getStatus() != null ? user.getStatus().name() : null;

        // role может отсутствовать в текущей модели; если поля нет, эту строку удалишь.
        dto.role = safeRole(user);

        return dto;
    }

    /**
     * Изолирует доступ к роли, чтобы DTO не ломался, если поле Role ещё не заведено.
     * При добавлении Role в сущность User метод можно упростить до user.getRole().name().
     */
    private static String safeRole(User user) {
        try {
            Object role = user.getClass().getMethod("getRole").invoke(user);
            return role != null ? role.toString() : null;
        } catch (Exception ignored) {
            return null;
        }
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public String getRole() {
        return role;
    }
}
