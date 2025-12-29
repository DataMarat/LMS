package com.example.lms.repository;

import com.example.lms.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с записями студентов на курсы ({@link Enrollment}).
 * <p>
 * Используется как низкоуровневый слой доступа к данным для сценариев:
 * <ul>
 *   <li>проверка наличия записи студента на курс,</li>
 *   <li>получение записи по паре student+course,</li>
 *   <li>получение всех записей по курсу / по студенту.</li>
 * </ul>
 */
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    /**
     * Проверяет, существует ли запись студента на курс.
     *
     * @param studentId идентификатор студента (User)
     * @param courseId  идентификатор курса (Course)
     * @return {@code true}, если запись существует; иначе {@code false}
     */
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     * Находит запись студента на курс по паре идентификаторов.
     *
     * @param studentId идентификатор студента (User)
     * @param courseId  идентификатор курса (Course)
     * @return {@link Optional} с записью, если найдена; иначе {@link Optional#empty()}
     */
    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     * Возвращает все записи на указанный курс.
     *
     * @param courseId идентификатор курса
     * @return список записей на курс (может быть пустым)
     */
    List<Enrollment> findAllByCourseId(Long courseId);

    /**
     * Возвращает все записи указанного студента на курсы.
     *
     * @param studentId идентификатор студента
     * @return список записей студента (может быть пустым)
     */
    List<Enrollment> findAllByStudentId(Long studentId);
}
