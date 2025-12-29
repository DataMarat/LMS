package com.example.lms.controller;

import com.example.lms.dto.UserResponse;
import com.example.lms.entity.User;
import com.example.lms.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API для чтения пользователей.
 * Создание пользователей на текущем этапе не требуется (данные загружаются из CSV).
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Возвращает список пользователей.
     */
    @GetMapping
    public List<UserResponse> getAll() {
        log.debug("HTTP GET /api/users invoked");
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    /**
     * Возвращает пользователя по id.
     *
     * @param id идентификатор пользователя
     * @return DTO пользователя
     * @throws EntityNotFoundException если пользователь не найден
     */
    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        log.debug("HTTP GET /api/users/{} invoked", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
        return UserResponse.from(user);
    }
}
