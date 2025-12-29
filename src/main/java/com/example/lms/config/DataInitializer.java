package com.example.lms.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Инициализация демо-данных из CSV.
 * <p>
 * Вставка выполняется через JdbcTemplate, чтобы гарантированно сохранять id из CSV
 * и не зависеть от @GeneratedValue(strategy = IDENTITY).
 * <p>
 * Ожидаемые CSV:
 * <ul>
 *     <li>data/users.csv: id,first_name,last_name,email,status,role</li>
 *     <li>data/courses.csv: id,title,description,teacher_id</li>
 *     <li>data/enrollments.csv: student_id,course_id</li>
 * </ul>
 */
@ConditionalOnProperty(
        name = "app.data.initializer.enabled",
        havingValue = "true",
        matchIfMissing = true
)
@Component
public class DataInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DataInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Integer usersCount = jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
        if (usersCount != null && usersCount > 0) {
            return;
        }

        insertUsersFromCsv();
        bumpIdentitySequence("users");

        insertCoursesFromCsv();
        bumpIdentitySequence("courses");

        insertEnrollmentsFromCsv();
        bumpIdentitySequenceIfExists("enrollments");
    }

    /**
     * users.csv
     * id,first_name,last_name,email,status,role
     */
    private void insertUsersFromCsv() throws Exception {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new ClassPathResource("data/users.csv").getInputStream(),
                        StandardCharsets.UTF_8
                )
        )) {
            reader.readLine(); // header
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] p = split(line);

                int id = Integer.parseInt(p[0]);
                String firstName = p[1];
                String lastName = p[2];
                String email = p[3];
                String status = p[4]; // ACTIVE / INACTIVE / ...
                String role = p[5];   // STUDENT / TEACHER / ADMIN

                jdbcTemplate.update(
                        "insert into users (id, first_name, last_name, email, status, role) values (?, ?, ?, ?, ?, ?)",
                        id, firstName, lastName, email, status, role
                );
            }
        }
    }

    /**
     * courses.csv
     * id,title,description
     */
    private void insertCoursesFromCsv() throws Exception {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new ClassPathResource("data/courses.csv").getInputStream(),
                        StandardCharsets.UTF_8
                )
        )) {
            reader.readLine(); // header
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] p = split(line);

                int id = Integer.parseInt(p[0]);
                String title = p[1];
                String description = p[2];
//                int teacherId = Integer.parseInt(p[3]);

                jdbcTemplate.update(
                        "insert into courses (id, title, description) values (?, ?, ?)",
                        id, title, description
//                        id, title, description, teacherId
                );
            }
        }
    }

    /**
     * enrollments.csv
     * student_id,course_id
     */
    private void insertEnrollmentsFromCsv() throws Exception {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new ClassPathResource("data/enrollments.csv").getInputStream(),
                        StandardCharsets.UTF_8
                )
        )) {
            reader.readLine(); // header
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] p = split(line);

                int studentId = Integer.parseInt(p[0]);
                int courseId = Integer.parseInt(p[1]);

                jdbcTemplate.update(
                        "insert into enrollments (student_id, course_id) values (?, ?)",
                        studentId, courseId
                );
            }
        }
    }

    /**
     * Поднимает значение identity/sequence до max(id) после ручной вставки id.
     * Иначе следующий insert без id может попытаться использовать уже занятый id.
     */
    private void bumpIdentitySequence(String tableName) {
        jdbcTemplate.execute(
                "select setval(pg_get_serial_sequence('" + tableName + "', 'id'), " +
                        "coalesce((select max(id) from " + tableName + "), 1), true)"
        );
    }

    /**
     * То же самое, но без падения, если у таблицы нет id/sequence.
     */
    private void bumpIdentitySequenceIfExists(String tableName) {
        try {
            bumpIdentitySequence(tableName);
        } catch (Exception ignored) {
            // например, если у enrollments нет id или sequence — пропускаем
        }
    }

    private String[] split(String line) {
        return line.split("\\s*,\\s*");
    }
}
