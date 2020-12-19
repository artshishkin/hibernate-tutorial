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

    @Transactional
    public void playWithEntityManagerDetach(){
        Course course1 = new Course("Play course 1");
        em.persist(course1);
        Course course2 = new Course("Play course 2");
        em.persist(course2);

        em.flush();

        course1.setName("Updated course 1");
        course2.setName("Updated course 2");
        em.detach(course2);
        em.flush();
    }

    @Transactional
    public void playWithEntityManagerClear(){
        Course course1 = new Course("Play course 1");
        em.persist(course1);
        Course course2 = new Course("Play course 2");
        em.persist(course2);

        em.flush();
        em.clear();

        course1.setName("Updated course 1");
        course2.setName("Updated course 2");
        em.flush();
    }

    @Transactional
    public void playWithEntityManagerRefresh(){
        Course course1 = new Course("Play course 1");
        em.persist(course1);
        Course course2 = new Course("Play course 2");
        em.persist(course2);

        em.flush();

        course1.setName("Updated course 1");
        course2.setName("Updated course 2");

        em.refresh(course2);
        em.flush();
    }
}