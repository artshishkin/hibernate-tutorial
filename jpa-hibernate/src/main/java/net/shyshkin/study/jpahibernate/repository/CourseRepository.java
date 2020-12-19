package net.shyshkin.study.jpahibernate.repository;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.jpahibernate.entity.Course;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class CourseRepository {

    private final EntityManager em;

    public Course findById(Long id) {
        return em.find(Course.class, id);
    }

    @Transactional
    public void deleteById(Long id) {
        Course course = findById(id);
        em.remove(course);
    }

    @Transactional
    public Course save(Course course) {

        if (course.getId() == null)
            em.persist(course);
        else
            em.merge(course);

        return course;
    }
}
