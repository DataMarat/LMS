package com.example.lms.it;

import org.springframework.test.context.ActiveProfiles;

/**
 * Базовая конфигурация интеграционных тестов.
 * Использует профиль test (H2 + create-drop).
 */
@ActiveProfiles("test")
public abstract class IntegrationTestBase {
}
