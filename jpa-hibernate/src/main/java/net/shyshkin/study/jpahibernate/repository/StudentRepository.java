package net.shyshkin.study.jpahibernate.repository;

import lombok.RequiredArgsConstructor;
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
}
