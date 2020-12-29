package net.shyshkin.study.jpahibernate.repository;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.jpahibernate.entity.Course;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@ComponentScan
class CriteriaQueryTest {

    @Autowired
    EntityManager em;

    @Autowired
    TestEntityManager tem;

    @Test
    void findAll_criteriaQuery() {
        //given
        //"select c from Course c"

        //1. Use Criteria Builder to create a Criteria Query
        //returning the expected result object
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = cb.createQuery(Course.class);

        //2. Define roots for tables which are involved in the query
        Root<Course> courseRoot = criteriaQuery.from(Course.class);

        //3. Define Predicates etc using Criteria Builder

        //4. Add Predicates etc to Criteria Query

        //5. Build the TypedQuery using the entity manager and criteria query
        TypedQuery<Course> query = em.createQuery(criteriaQuery.select(courseRoot));

        //when
        List<Course> courses = query.getResultList();

        //then
        assertThat(courses).hasSize(4)
                .allSatisfy(course -> log.info("{}", course))
                .allSatisfy(course -> assertThat(course).hasNoNullFieldsOrProperties());
    }

    @Test
    void findCoursesLikeNATE() {
        //given
        //"select c from Course c where c.name like '%nate'" -> Hibernate

        //1. Use Criteria Builder to create a Criteria Query
        //returning the expected result object
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = cb.createQuery(Course.class);

        //2. Define roots for tables which are involved in the query
        Root<Course> courseRoot = criteriaQuery.from(Course.class);

        //3. Define Predicates etc using Criteria Builder
        Predicate likeNATE = cb.like(courseRoot.get("name"), "%nate");

        //4. Add Predicates etc to Criteria Query
        criteriaQuery.where(likeNATE);

        //5. Build the TypedQuery using the entity manager and criteria query
        TypedQuery<Course> query = em.createQuery(criteriaQuery.select(courseRoot));

        //when
        List<Course> courses = query.getResultList();

        //then
        assertThat(courses)
                .hasSize(1)
                .allSatisfy(course -> log.info("{}", course))
                .allSatisfy(course -> assertThat(course.getName()).containsIgnoringCase("nate"));
    }

    @Test
    void findCoursesWithoutStudents() {
        //given
        //"select c from Course c where c.students is empty"

        //1. Use Criteria Builder to create a Criteria Query
        //returning the expected result object
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = cb.createQuery(Course.class);

        //2. Define roots for tables which are involved in the query
        Root<Course> courseRoot = criteriaQuery.from(Course.class);

        //3. Define Predicates etc using Criteria Builder
        Predicate studentsIsEmpty = cb.isEmpty(courseRoot.get("students"));

        //4. Add Predicates etc to Criteria Query
        criteriaQuery.where(studentsIsEmpty);

        //5. Build the TypedQuery using the entity manager and criteria query
        TypedQuery<Course> query = em.createQuery(criteriaQuery.select(courseRoot));

        //when
        List<Course> courses = query.getResultList();

        //then
        assertThat(courses)
                .hasSize(1)
                .allSatisfy(course -> assertThat(course.getStudents()).isEmpty())
                .allMatch(course -> course.getName().equals("AWS Developer"));
    }


    @Test
    @DisplayName("Course join Student will NOT return Rows with Courses without student (INNER JOIN)")
    void join() {
        //given
        //"select c, s from Course c join c.students s"

        //1. Use Criteria Builder to create a Criteria Query
        //returning the expected result object
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = cb.createQuery(Course.class);

        //2. Define roots for tables which are involved in the query
        Root<Course> courseRoot = criteriaQuery.from(Course.class);

        //3. Define Predicates etc using Criteria Builder
        courseRoot.join("students");

        //4. Add Predicates etc to Criteria Query

        //5. Build the TypedQuery using the entity manager and criteria query
        TypedQuery<Course> query = em.createQuery(criteriaQuery.select(courseRoot));

        //when
        List<Course> courses = query.getResultList();

        //then
        assertThat(courses)
                .hasSize(7);
    }

    @Test
    @DisplayName("Course left join Student WILL return Rows with Courses without student (LEFT OUTER JOIN)")
    void left_join() {
        //given
        //"select c, s from Course c left join c.students s"

        //1. Use Criteria Builder to create a Criteria Query
        //returning the expected result object
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = cb.createQuery(Course.class);

        //2. Define roots for tables which are involved in the query
        Root<Course> courseRoot = criteriaQuery.from(Course.class);

        //3. Define Predicates etc using Criteria Builder
        courseRoot.join("students", JoinType.LEFT);

        //4. Add Predicates etc to Criteria Query

        //5. Build the TypedQuery using the entity manager and criteria query
        TypedQuery<Course> query = em.createQuery(criteriaQuery.select(courseRoot));

        //when
        List<Course> courses = query.getResultList();

        //then
        assertThat(courses)
                .hasSize(8);
    }
}