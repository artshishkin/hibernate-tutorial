package net.shyshkin.study.jpahibernate.repository;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.jpahibernate.entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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


}