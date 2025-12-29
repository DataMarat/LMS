package com.example.lms.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Интеграционные тесты REST API для enrollments на H2.
 */
@SpringBootTest
@AutoConfigureMockMvc
class EnrollmentApiIT extends IntegrationTestBase {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Удаляем в порядке FK-зависимостей
        jdbcTemplate.execute("DELETE FROM enrollments");
        jdbcTemplate.execute("DELETE FROM courses");
        jdbcTemplate.execute("DELETE FROM users");

        // Users (id Long)
        // NB: если role ещё не добавлен в users — убери role из insert-ов.
        jdbcTemplate.update(
                "INSERT INTO users (id, first_name, last_name, email, status, role) VALUES (?,?,?,?,?,?)",
                1L, "Admin", "User", "admin@example.com", "ACTIVE", "ADMIN"
        );
        jdbcTemplate.update(
                "INSERT INTO users (id, first_name, last_name, email, status, role) VALUES (?,?,?,?,?,?)",
                2L, "John", "Teacher", "teacher@example.com", "ACTIVE", "TEACHER"
        );
        jdbcTemplate.update(
                "INSERT INTO users (id, first_name, last_name, email, status, role) VALUES (?,?,?,?,?,?)",
                3L, "Alice", "Student", "alice@example.com", "ACTIVE", "STUDENT"
        );

        // Courses
        jdbcTemplate.update(
                "INSERT INTO courses (id, title, description, teacher_id) VALUES (?,?,?,?)",
                1L, "Java Basics", "Intro to Java", 2L
        );
        jdbcTemplate.update(
                "INSERT INTO courses (id, title, description, teacher_id) VALUES (?,?,?,?)",
                2L, "Spring Boot", "Spring Boot basics", 2L
        );
    }

    @Test
    void enroll_shouldCreateEnrollment_andReturn201() throws Exception {
        String body = objectMapper.writeValueAsString(new EnrollmentReq(3L, 1L));

        mockMvc.perform(post("/api/enrollments")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.studentId").value(3))
                .andExpect(jsonPath("$.courseId").value(1));
    }

    @Test
    void enroll_duplicate_shouldReturn409() throws Exception {
        jdbcTemplate.update("INSERT INTO enrollments (student_id, course_id) VALUES (?,?)", 3L, 1L);

        String body = objectMapper.writeValueAsString(new EnrollmentReq(3L, 1L));

        mockMvc.perform(post("/api/enrollments")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"));
    }

    @Test
    void getStudents_shouldReturnStudentList_forCourse() throws Exception {
        jdbcTemplate.update("INSERT INTO enrollments (student_id, course_id) VALUES (?,?)", 3L, 1L);

        mockMvc.perform(get("/api/courses/1/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].email").value("alice@example.com"));
    }

    @Test
    void unenroll_shouldReturn204_andRemoveEnrollment() throws Exception {
        jdbcTemplate.update("INSERT INTO enrollments (student_id, course_id) VALUES (?,?)", 3L, 1L);

        String body = objectMapper.writeValueAsString(new EnrollmentReq(3L, 1L));

        mockMvc.perform(delete("/api/enrollments")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/courses/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    static final class EnrollmentReq {
        public Long studentId;
        public Long courseId;

        EnrollmentReq(Long studentId, Long courseId) {
            this.studentId = studentId;
            this.courseId = courseId;
        }
    }
}
