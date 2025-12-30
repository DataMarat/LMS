# LMS на Java

Серверное приложение учебной платформы (LMS) на Spring Boot с использованием Hibernate/JPA и PostgreSQL.  
Проект демонстрирует типовой стек: слоистая архитектура (web -> service -> repository -> entity), работа с ORM-связями, REST API, централизованная обработка ошибок, предзаполнение данных и интеграционные тесты.

---

## Технологический стек

- Java 17+
- Spring Boot 3.x
- Spring Data JPA (Hibernate ORM)
- PostgreSQL (профили `dev`/`prod`)
- H2 (профиль `test` для интеграционных тестов)
- Springdoc OpenAPI (Swagger UI)
- Maven

---

## Функциональность

### Модель данных и ORM
- Сущности предметной области LMS (минимальный набор для полного сценария “пользователь → курс → запись”):
  - **User** (роль/статус, email)
  - **Course** (преподаватель, описание)
  - **Enrollment** (запись студента на курс, ограничение уникальности пары student+course)
- Связи JPA настроены через `@ManyToOne`/`@OneToMany` и используются в бизнес-сценариях (запись/просмотр студентов курса).

### Репозитории и CRUD
- Репозитории реализованы на базе Spring Data JPA (`JpaRepository`) для ключевых сущностей.
- CRUD-операции доступны через сервисный слой и REST API (см. раздел «REST API»).

### Запись на курс (Enrollment)
- Запись студента на курс с проверкой уникальности.
- Отписка от курса.
- Получение списка студентов, записанных на курс.

---

## REST API

Базовый префикс API: `/api`

### Users
- `GET /api/users` — список пользователей
- `GET /api/users/{id}` — пользователь по id

### Courses
- `GET /api/courses` — список курсов
- `GET /api/courses/{id}` — курс по id
- `GET /api/courses/{courseId}/students` — список студентов курса

### Enrollments
- `POST /api/enrollments` — запись студента на курс  
  Пример тела запроса:
  ```json
  {
    "studentId": 3,
    "courseId": 1
  }
  ```
- `DELETE /api/enrollments` — отписка студента от курса  
  Пример тела запроса:
  ```json
  {
    "studentId": 3,
    "courseId": 1
  }
  ```

### Коды ответов и ошибки
- Используются корректные HTTP-коды (`200/201/204/400/404/409`).
- Ошибки обрабатываются централизованно (ControllerAdvice), ответы об ошибках формируются в едином формате.

---

## Swagger / OpenAPI

После запуска приложения:
- Swagger UI: `http://localhost:8080/swagger-ui`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

---

## Предзаполнение данными

Проект поддерживает предзаполнение демонстрационными данными из CSV для быстрого старта «из коробки».

Используемые наборы данных (минимально необходимый сценарий):
- `users.csv` — пользователи (включая преподавателя и студентов)
- `courses.csv` — курсы (с привязкой к преподавателю)
- `enrollments.csv` — записи студентов на курсы

---

## Конфигурация приложения (PostgreSQL)

Проект настраивается через **Spring Profiles** и **переменные окружения**. Чувствительные данные (логин/пароль к БД) **не хранятся в коде** и **не коммитятся** в репозиторий.

### Профили

- **dev** — разработка: PostgreSQL, удобные логи, можно включать автосоздание схемы/инициализацию
- **test** — тесты: H2 (in-memory), без PostgreSQL и без Docker
- *(опционально)* **prod** — прод: PostgreSQL строго через env, без автогенерации схемы

Профиль выбирается переменной окружения:

- `SPRING_PROFILES_ACTIVE=dev|test|prod`

> Если профиль не задан, Spring Boot использует профиль по умолчанию (обычно `dev`, если так настроено в `application.yml`).

---

### Переменные окружения для PostgreSQL

- `DB_URL` — JDBC URL (пример: `jdbc:postgresql://<host>:5432/<db>`)
- `DB_USERNAME` — пользователь БД
- `DB_PASSWORD` — пароль БД

Эти переменные подставляются в `application-dev.yml` / `application-prod.yml` через плейсхолдеры Spring вида `${DB_URL}`, `${DB_USERNAME}`, `${DB_PASSWORD}`.

---

## Запуск приложения

### PowerShell (построчно)

```powershell
$env:SPRING_PROFILES_ACTIVE="dev"
$env:DB_URL="jdbc:postgresql://<host>:5432/<db>"
$env:DB_USERNAME="<user>"
$env:DB_PASSWORD="<password>"

mvn spring-boot:run
```

### PowerShell (в одну строку)

```powershell
$env:SPRING_PROFILES_ACTIVE="dev"; $env:DB_URL="jdbc:postgresql://<host>:5432/<db>"; $env:DB_USERNAME="<user>"; $env:DB_PASSWORD="<password>"; mvn spring-boot:run
```

---

### CMD (построчно)

```cmd
set SPRING_PROFILES_ACTIVE=dev
set DB_URL=jdbc:postgresql://<host>:5432/<db>
set DB_USERNAME=<user>
set DB_PASSWORD=<password>

mvn spring-boot:run
```

### CMD (в одну строку)

```cmd
set SPRING_PROFILES_ACTIVE=dev&& set DB_URL=jdbc:postgresql://<host>:5432/<db>&& set DB_USERNAME=<user>&& set DB_PASSWORD=<password>&& mvn spring-boot:run
```

---

### Linux/macOS (bash/zsh) в одну строку

```bash
SPRING_PROFILES_ACTIVE=dev DB_URL="jdbc:postgresql://<host>:5432/<db>" DB_USERNAME="<user>" DB_PASSWORD="<password>" mvn spring-boot:run
```

---

## Переключение профилей

### Запуск с профилем `dev`

```bash
SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run
```

### Запуск с профилем `prod` (все параметры обязаны быть переданы через env)

```bash
SPRING_PROFILES_ACTIVE=prod DB_URL="jdbc:postgresql://<host>:5432/<db>" DB_USERNAME="<user>" DB_PASSWORD="<password>" mvn spring-boot:run
```

### Запуск тестов (профиль `test`)

Интеграционные тесты настроены на профиль `test` и используют H2.

```bash
mvn test
```

> Если нужно принудительно указать профиль для тестов через Maven:
> - Windows PowerShell: `$env:SPRING_PROFILES_ACTIVE="test"; mvn test`
> - Linux/macOS: `SPRING_PROFILES_ACTIVE=test mvn test`

---

## Гигиена секретов

- **Не коммить** реальные креды.
- Локальные значения можно хранить в `.env` (не коммитить) и добавить в `.gitignore`:
  - `.env`
  - `.env.*`
- В репозиторий можно положить шаблон `.env.example` только с плейсхолдерами.

---

## Тестирование

### Интеграционные тесты
Интеграционные тесты поднимают контекст Spring и проверяют ключевые сценарии CRUD на H2 (профиль `test`):
- создание записи Enrollment
- проверка уникальности (конфликт при дубле)
- чтение списка студентов курса
- удаление записи Enrollment

### Запуск тестов
```bash
mvn test
```

---

## Архитектура и структура проекта

Рекомендуемая структура:
- `entity` — JPA-сущности и enum-типы
- `repository` — Spring Data репозитории
- `service` — бизнес-логика и транзакционные операции
- `web/controller` — REST-контроллеры и обработка ошибок
- `config` — конфигурация приложения (инициализация данных, OpenAPI и т.п.)

Ключевые принципы:
- Разделение ответственности по слоям (SRP).
- DI через конструктор.
- Валидация входных DTO на уровне web-слоя.
- Централизованная обработка исключений.

---

## Быстрый старт

1) Убедиться, что доступна PostgreSQL (профиль `dev`) или использовать тестовый профиль.
2) Запустить приложение (см. раздел «Конфигурация приложения (PostgreSQL)»).
3) Открыть Swagger UI и проверить эндпойнты.

---

## TODO
- добавить Flyway/Liquibase (упорядоченные колонки в таблицах)


## Лицензия

Учебный проект.


