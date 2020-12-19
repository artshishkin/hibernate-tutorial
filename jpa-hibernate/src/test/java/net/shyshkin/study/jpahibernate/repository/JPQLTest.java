package net.shyshkin.study.jpahibernate.repository;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.jpahibernate.entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@ComponentScan
class JPQLTest {

    @Autowired
    EntityManager em;

    @Test
    void findAll_basic() {
        //when
        Query query = em.createQuery("select c from Course c");
        List courses = query.getResultList();


        //then
        assertThat(courses).hasSize(2)
                .allSatisfy(course -> log.info("{}", course))
                .allSatisfy(course -> assertThat(course).hasNoNullFieldsOrProperties());
    }

    @Test
    void findAll_typed() {
        //when
        TypedQuery<Course> typedQuery = em.createQuery("select c from Course c", Course.class);
        List<Course> courses = typedQuery.getResultList();


        //then
        assertThat(courses)
                .hasSize(2)
                .allSatisfy(course -> log.info("{}", course))
                .allSatisfy(course -> assertThat(course).hasNoNullFieldsOrProperties());
    }

    @Test
    void find_where() {
        //when
        TypedQuery<Course> typedQuery = em.createQuery("select c from Course c where c.name like '%nate'", Course.class);
        List<Course> courses = typedQuery.getResultList();


        //then
        assertThat(courses)
                .hasSize(1)
                .allSatisfy(course -> log.info("{}", course))
                .allSatisfy(course ->
                        assertThat(course)
                                .hasNoNullFieldsOrProperties()
                                .hasFieldOrPropertyWithValue("name","Hibernate"));
    }

    private void syncDB() {
        em.flush();
        em.clear();
    }
}