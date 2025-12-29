package com.example.lms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Точка входа в приложение LMS.
 * Инициирует запуск Spring Boot и регистрацию компонентов приложения.
 */
@SpringBootApplication
public class LmsApplication {

    private static final Logger log = LoggerFactory.getLogger(LmsApplication.class);

    public static void main(String[] args) {
        log.info("Starting LMS application");
        SpringApplication.run(LmsApplication.class, args);
        log.info("LMS application started successfully");
    }
}
