package net.shyshkin.study.jpahibernate.repository;

import net.shyshkin.study.jpahibernate.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.TypedQuery;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ComponentScan
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    TestEntityManager tem;

    @Test
    void createStudentWithPassport() {
        //given
        String expectedNumber = "ABC123";
        String stName = "Olya";

        //when
        studentRepository.createStudentWithPassport();

        //then
        TypedQuery<Student> studentTypedQuery = tem.getEntityManager()
                .createQuery("select s from Student s where s.name=:name", Student.class)
                .setParameter("name", stName);

        Student student = studentTypedQuery.getSingleResult();
        assertThat(student).isNotNull().hasNoNullFieldsOrProperties();
        assertThat(student.getPassport().getNumber()).isEqualTo(expectedNumber);

        tem.flush();
        tem.clear();
    }
}