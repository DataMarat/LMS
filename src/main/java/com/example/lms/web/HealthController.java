package com.example.lms.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер, предоставляющий диагностический HTTP-эндпойнт
 * для проверки доступности приложения.
 */
@RestController
public class HealthController {

    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    /**
     * Возвращает диагностическое сообщение, подтверждающее,
     * что приложение успешно запущено и отвечает на запросы.
     *
     * @return строковое сообщение о состоянии приложения
     */
    @GetMapping("/api/health")
    public String health() {
        log.debug("Received request for application health check");
        return "LMS is running";
    }
}
