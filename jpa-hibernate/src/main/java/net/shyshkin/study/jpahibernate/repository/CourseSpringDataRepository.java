package net.shyshkin.study.jpahibernate.repository;

import net.shyshkin.study.jpahibernate.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CourseSpringDataRepository extends JpaRepository<Course, Long> {

    List<Course> findByName(String name);

    long countByName(String name);

    List<Course> findByNameContainingAndAndCreatedDateAfter(String namePart, LocalDateTime date);

    List<Course> findByNameContainingOrderByNameDesc(String namePart);

    void deleteByName(String name);

}
