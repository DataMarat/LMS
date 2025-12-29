package com.example.lms.repository;

import com.example.lms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий для доступа к данным сущности {@link User}.
 * Предоставляет базовые CRUD-операции и дополнительные методы поиска.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по адресу электронной почты.
     *
     * @param email адрес электронной почты
     * @return необязательное значение с найденным пользователем
     */
    Optional<User> findByEmail(String email);
}
