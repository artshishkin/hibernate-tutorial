package net.shyshkin.study.jpahibernate.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("N + 1 Problem Test")
@DirtiesContext
class NPlusOneProblemTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Nested
    @DisplayName("One To One")
    class TestOneToOneRelationship{

        @Test
        @DisplayName("N+1 problem: Assume we have goal to get all students with certain condition (i.e. age>21) and get their passports")
        void retrieveAllStudentsWithPassports_N_plus_One_Problem() {
            //given
            EntityManager em = testEntityManager.getEntityManager();

            //when
            TypedQuery<Student> typedQuery = em.createQuery("select s from Student s", Student.class);

            //then
            List<Student> students = typedQuery.getResultList();
            assertThat(students)
                    .hasSize(3)
                    .allSatisfy(student -> assertThat(student.getPassport().getNumber()).isNotEmpty());
        }

        @Test
        @DisplayName("N+1 problem: Assume we have goal to get all passports with certain condition (i.e. number starts with 'AB') and get their owners")
        void retrieveAllPassportsWithStudents_N_plus_One_Problem() {
            //given
            EntityManager em = testEntityManager.getEntityManager();

            //when
            TypedQuery<Passport> typedQuery = em.createQuery("select p from Passport p", Passport.class);

            //then
            List<Passport> passports = typedQuery.getResultList();
            assertThat(passports)
                    .hasSize(3)
                    .allSatisfy(passport -> assertThat(passport.getStudent().getName()).isNotEmpty());
        }

        @Test
        @DisplayName("NO problem: Assume we have goal to get all students with certain condition (i.e. age>21) and get their passports")
        void retrieveAllStudentsWithPassports_NO_Problem() {
            //given
            EntityManager em = testEntityManager.getEntityManager();

            //when
            TypedQuery<Student> typedQuery = em.createQuery("select s from Student s join fetch s.passport", Student.class);

            //then
            List<Student> students = typedQuery.getResultList();
            assertThat(students)
                    .hasSize(3)
                    .allSatisfy(student -> assertThat(student.getPassport().getNumber()).isNotEmpty());
        }

        @Test
        @DisplayName("NO problem: Assume we have goal to get all passports with certain condition (i.e. number starts with 'AB') and get their owners")
        void retrieveAllPassportsWithStudents_NO_Problem() {
            //given
            EntityManager em = testEntityManager.getEntityManager();

            //when
            TypedQuery<Passport> typedQuery = em.createQuery("select p from Passport p join fetch p.student", Passport.class);

            //then
            List<Passport> passports = typedQuery.getResultList();
            assertThat(passports)
                    .hasSize(3)
                    .allSatisfy(passport -> assertThat(passport.getStudent().getName()).isNotEmpty());
        }
    }


    @Nested
    @DisplayName("Many To One")
    class TestManyToOneRelationship {

        @Test
        void retrieveAllReviews_N_plus_One_Problem() {
            //given
            EntityManager em = testEntityManager.getEntityManager();

            //when
            TypedQuery<Review> typedQuery = em.createQuery("select r from Review r where r.description like '%o%'", Review.class);

            //then
            List<Review> reviews = typedQuery.getResultList();
            assertThat(reviews)
                    .hasSize(2)
                    .allSatisfy(review -> assertThat(review.getCourse().getName()).isNotEmpty());
        }

        @Test
        void retrieveAllReviews_No_Problem() {
            //given
            EntityManager em = testEntityManager.getEntityManager();

            //when
            TypedQuery<Review> typedQuery = em.createQuery("select r from Review r join fetch r.course c where r.description like '%o%'", Review.class);

            //then
            List<Review> reviews = typedQuery.getResultList();
            assertThat(reviews)
                    .hasSize(2)
                    .allSatisfy(review -> assertThat(review.getCourse().getName()).isNotEmpty());
        }
    }
}