package net.shyshkin.study.jpahibernate.repository;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.jpahibernate.entity.Course;
import net.shyshkin.study.jpahibernate.entity.Passport;
import net.shyshkin.study.jpahibernate.entity.Student;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class StudentRepository {

    private final EntityManager em;

    public Student findById(Long id) {
        return em.find(Student.class, id);
    }

    @Transactional
    public void deleteById(Long id) {
        Student student = findById(id);
        em.remove(student);
    }

    @Transactional
    public Student save(Student student) {

        if (student.getId() == null)
            em.persist(student);
        else
            em.merge(student);

        return student;
    }

    @Transactional
    public void createStudentWithPassport() {
        Passport passport = new Passport("ABC123");
        em.persist(passport);

        Student student = new Student("Olya");
        student.setPassport(passport);
        em.persist(student);
    }

    @Transactional
    public void someOperationToUnderstandPersistenceContext() {
        Student student = em.find(Student.class, 20001L);
        //Persistence context (student)

        Passport passport = student.getPassport();
        //Persistence context (student, passport)

        passport.setNumber("Foo123");
        //Persistence context (student, passport++)

        student.setName("Buzz Bar");
        //Persistence context (student++, passport++)
    }

    @Transactional
    public void insertHardcodedStudentAndCourse() {
        Student student = new Student("JackDocker");
        Course course = new Course("Docker");
//        student.addCourse(course);
        course.addStudent(student);

        em.persist(student);
        em.persist(course);
    }

    @Transactional
    public void insertStudentAndCourse(Student student, Course course) {
        student.addCourse(course);
//        course.addStudent(student);

        em.persist(student);
        em.persist(course);
    }
}
