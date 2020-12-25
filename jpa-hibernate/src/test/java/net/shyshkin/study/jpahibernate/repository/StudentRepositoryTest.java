package net.shyshkin.study.jpahibernate.repository;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.jpahibernate.entity.Course;
import net.shyshkin.study.jpahibernate.entity.Passport;
import net.shyshkin.study.jpahibernate.entity.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
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

        syncDB();
    }

    @Test
    @DisplayName("OneToOne relationship is EAGER by default")
    void retrieveStudentWithDetails() {
        //given
        long studentId = 20001L;

        //when
        Student student = tem.find(Student.class, studentId);

        //then
        assertThat(student.getPassport())
                .isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @Transactional
    @DisplayName("Bidirectional OneToOne relationship with Lazy fetch")
    void retrievePassportAndAssociatedStudent() {
        //given
        long passportId = 30001L;

        //when
        Passport passport = tem.find(Passport.class, passportId);

        //then
        assertThat(passport.getStudent())
                .isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    void retrieveStudentWithCourses() {
        //given
        Long studentId = 20001L;

        //when
        Student student = studentRepository.findById(studentId);

        //then
        assertThat(student.getCourses())
                .hasSize(3)
                .allSatisfy(course -> assertThat(course.getName()).isNotEmpty());
    }

    @Test
    void retrieveCourseWithStudents() {
        //given
        Long courseId = 10001L;

        //when
        Course course = tem.find(Course.class, courseId);

        //then
        assertThat(course.getStudents())
                .hasSize(2)
                .allSatisfy(student -> assertThat(student.getName()).isNotEmpty());
    }

    @Test
    void insertHardcodedStudentAndCourse() {
        //given
        EntityManager em = tem.getEntityManager();

        //when
        studentRepository.insertHardcodedStudentAndCourse();

        //then
        TypedQuery<Student> findStudentByName = em
                .createNamedQuery("findStudentByName", Student.class)
                .setParameter("name", "JackDocker");

        syncDB();

        Student student = findStudentByName.getSingleResult();
        assertThat(student.getCourses())
                .hasSize(1)
                .allSatisfy(course -> assertThat(course.getName()).isEqualTo("Docker"));
    }

    @Test
    void insertStudentAndCourse() {
        //given
        EntityManager em = tem.getEntityManager();
        Student studentToSave = new Student("JackDocker");
        Course courseToSave = new Course("Docker");

        //when
        studentRepository.insertStudentAndCourse(studentToSave, courseToSave);

        //then
        TypedQuery<Student> findStudentByName = em
                .createNamedQuery("findStudentByName", Student.class)
                .setParameter("name", "JackDocker");

        syncDB();

        Student student = findStudentByName.getSingleResult();
        assertThat(student.getCourses())
                .hasSize(1)
                .allSatisfy(course -> assertThat(course.getName()).isEqualTo("Docker"));
    }

    private void syncDB() {
        tem.flush();
        tem.clear();
    }

}