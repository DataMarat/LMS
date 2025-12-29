package com.example.lms.service;

import com.example.lms.entity.User;
import com.example.lms.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Сервисный слой для работы с сущностью Person.
 * Инкапсулирует операции сохранения и получения данных.
 */
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    /**
     * Создаёт новый экземпляр сервиса с заданным репозиторием.
     *
     * @param userRepository репозиторий для доступа к данным Person
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Сохраняет нового пользователя в базе данных.
     *
     * @param user сущность пользователя
     * @return сохранённая сущность с заполненным идентификатором
     */
    public User create(User user) {
        log.info("Creating new user with email={}", user.getEmail());
        return userRepository.save(user);
    }

    /**
     * Возвращает пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return необязательное значение с найденным пользователем
     */
    public Optional<User> findById(Long id) {
        log.info("Searching for user with id={}", id);
        return userRepository.findById(id);
    }
}
