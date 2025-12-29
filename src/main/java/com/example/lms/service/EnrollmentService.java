package com.example.lms.service;

import com.example.lms.entity.Course;
import com.example.lms.entity.Enrollment;
import com.example.lms.entity.User;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.EnrollmentRepository;
import com.example.lms.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Сервисный слой для управления записями студентов на курсы ({@link Enrollment}).
 * <p>
 * Содержит бизнес-правила:
 * <ul>
 *   <li>нельзя записать несуществующего студента на несуществующий курс,</li>
 *   <li>нельзя создать дублирующую запись (student+course),</li>
 *   <li>можно отписаться от курса (удалить enrollment).</li>
 * </ul>
 */
@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            UserRepository userRepository,
            CourseRepository courseRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * Записывает студента на курс.
     *
     * @param studentId идентификатор студента
     * @param courseId  идентификатор курса
     * @return созданная запись {@link Enrollment}
     * @throws EntityNotFoundException если студент или курс не найдены
     * @throws IllegalStateException   если студент уже записан на курс
     */
    @Transactional
    public Enrollment enroll(Long studentId, Long courseId) {
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new IllegalStateException("Student is already enrolled to this course.");
        }

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found: " + studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + courseId));

        Enrollment enrollment = new Enrollment(student, course);
        return enrollmentRepository.save(enrollment);
    }

    /**
     * Отписывает студента от курса (удаляет запись).
     *
     * @param studentId идентификатор студента
     * @param courseId  идентификатор курса
     * @throws EntityNotFoundException если запись enrollment не найдена
     */
    @Transactional
    public void unenroll(Long studentId, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Enrollment not found for student=" + studentId + " and course=" + courseId
                ));

        enrollmentRepository.delete(enrollment);
    }

    /**
     * Возвращает все записи на конкретный курс.
     *
     * @param courseId идентификатор курса
     * @return список записей на курс (может быть пустым)
     */
    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.findAllByCourseId(courseId);
    }

    /**
     * Возвращает все записи конкретного студента на курсы.
     *
     * @param studentId идентификатор студента
     * @return список записей студента (может быть пустым)
     */
    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepository.findAllByStudentId(studentId);
    }
}
