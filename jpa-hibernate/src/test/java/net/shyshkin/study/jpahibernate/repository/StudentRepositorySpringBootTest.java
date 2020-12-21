package net.shyshkin.study.jpahibernate.repository;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.jpahibernate.entity.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("spring_boot_test")
class StudentRepositorySpringBootTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    EntityManager em;

    @Test
    @DirtiesContext
    void createStudentWithPassport() {
        //given
        String expectedNumber = "ABC123";
        String stName = "Olya";

        //when
        studentRepository.createStudentWithPassport();

        //then
        TypedQuery<Student> studentTypedQuery = em
                .createQuery("select s from Student s where s.name=:name", Student.class)
                .setParameter("name", stName);

        Student student = studentTypedQuery.getSingleResult();
        assertThat(student).isNotNull().hasNoNullFieldsOrProperties();
        assertThat(student.getPassport().getNumber()).isEqualTo(expectedNumber);
    }

    @Test
    @DisplayName("OneToOne relationship is EAGER by default")
    void retrieveStudentWithDetails() {
        //given
        long studentId = 20001L;

        //when
        Student student = em.find(Student.class, studentId);

        //then
        assertThat(student.getPassport())
                .isNotNull()
                .hasNoNullFieldsOrProperties();
    }
}