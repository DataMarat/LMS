package com.example.lms.repository;

import com.example.lms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для управления сущностями {@link Course}.
 * <p>
 * Предоставляет базовые операции CRUD, а также методы поиска
 * по коду курса и по преподавателю.
 */
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * Находит курс по его уникальному коду.
     *
     * @param id уникальный символьный код курса
     * @return необязательный результат с найденным курсом
     */
//    Optional<Course> findByCode(Long id);

    /**
     * Возвращает список курсов, которые ведёт указанный преподаватель.
     *
     * @param teacherId идентификатор пользователя-преподавателя
     * @return список курсов, связанных с данным преподавателем
     */
//    List<Course> findByTeacherId(Long teacherId);
}
