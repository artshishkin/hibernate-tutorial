package net.shyshkin.study.jpahibernate.repository;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.jpahibernate.entity.Address;
import net.shyshkin.study.jpahibernate.entity.Passport;
import net.shyshkin.study.jpahibernate.entity.Student;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
@ActiveProfiles("spring_boot_test")
class StudentRepositorySpringBootTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    EntityManager em;

    @Test
    @Transactional
    @DisplayName("OneToOne relationship with LAZY fetch must be wrapped in TRANSACTION if we want to access relations")
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
//    @Disabled("When FETCH type set to LAZY this will throw LazyInitializationException")
    @DisplayName("OneToOne relationship is EAGER by default")
    void retrieveStudentWithDetails_eager() {
        //given
        long studentId = 20001L;

        //when
        Student student = em.find(Student.class, studentId);

        //then
        assertThat(student.getPassport())
                .isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @Disabled("Now fetch set to EAGER")
    @DisplayName("OneToOne relationship with LAZY fetch will throw LazyInitializationException")
    void retrieveStudentWithDetails_exception() {
        //given
        long studentId = 20001L;

        //when
        Student student = em.find(Student.class, studentId);

        //then
        assertThatThrownBy(
                () -> {
                    Passport passport = student.getPassport();
                    log.info("Passport number : {}", passport.getNumber());
                })
                .isInstanceOf(LazyInitializationException.class)
                .hasMessageContaining("no Session");
    }

    @Test
    @Transactional
    @DisplayName("OneToOne relationship with LAZY fetch must be wrapped in TRANSACTION if we want to access relations")
    void retrieveStudentWithDetails_transaction() {
        //given
        long studentId = 20001L;

        //when
        Student student = em.find(Student.class, studentId);

        //then
        assertThat(student.getPassport())
                .isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    //Session & SessionFactory
    //EntityManager & Persistence Context
    //Transaction
    @Test
    @DirtiesContext
    void persistenceContextTest() {
        //given
        Long id = 20001L;

        //when
        studentRepository.someOperationToUnderstandPersistenceContext();

        //then
        Student student = em.find(Student.class, id);
        assertThat(student.getName()).isEqualTo("Buzz Bar");
    }

    @Test
    @Transactional
    @DisplayName("Setting Student Address trough EntityManager")
    void setAddressDetails_EntityManager() {
        //given
        long studentId = 20002L;
        Student student = em.find(Student.class, studentId);
        Address address = new Address("Line 1 Foo", "Line 2 Bar", "City Buzz");

        //when
        student.setAddress(address);

        //then
        em.flush();
        em.clear();

        Student studentUpdated = em.find(Student.class, studentId);
        assertThat(studentUpdated.getAddress())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("line1", "Line 1 Foo")
                .hasFieldOrPropertyWithValue("line2", "Line 2 Bar")
                .hasFieldOrPropertyWithValue("city", "City Buzz");
    }

    @Test
    @DisplayName("Setting Student Address trough Repository")
    void setAddressDetails_Repository() {
        //given
        long studentId = 20003L;
        Student student = studentRepository.findById(studentId);
        Address address = new Address("Line 1 Foo", "Line 2 Bar", "City Buzz");

        //when
        student.setAddress(address);
        studentRepository.save(student);

        //then
        Student studentUpdated = em.find(Student.class, studentId);
        assertThat(studentUpdated.getAddress())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("line1", "Line 1 Foo")
                .hasFieldOrPropertyWithValue("line2", "Line 2 Bar")
                .hasFieldOrPropertyWithValue("city", "City Buzz");
    }
}