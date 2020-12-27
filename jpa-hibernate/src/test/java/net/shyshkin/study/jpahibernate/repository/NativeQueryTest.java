package net.shyshkin.study.jpahibernate.repository;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.jpahibernate.entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@ComponentScan
class NativeQueryTest {

    @Autowired
    EntityManager em;

    @Test
    void native_queries_basic() {
        //when
        Query query = em.createNativeQuery("select * from course", Course.class);
        List courses = query.getResultList();

        //then
        assertThat(courses).hasSize(4)
                .allSatisfy(course -> log.info("{}", course))
                .allSatisfy(course -> assertThat(course).hasNoNullFieldsOrProperties());
    }

    @Test
    void native_queries_with_parameter() {
        //given
        Long id = 10001L;

        //when
        Query query = em.createNativeQuery("select * from course where id=?", Course.class);
        query.setParameter(1, id);
        Course course = (Course) query.getSingleResult();

        //then
        assertThat(course).isNotNull().hasNoNullFieldsOrProperties();
    }

    @Test
    void native_queries_with_named_parameter() {
        //given
        Long id = 10002L;

        //when
        Query query = em.createNativeQuery("select * from course where id=:id", Course.class);
        query.setParameter("id", id);
        Course course = (Course) query.getSingleResult();

        //then
        assertThat(course).isNotNull().hasNoNullFieldsOrProperties();
    }

    @Test
    void native_queries_update() {
        //when
        Query query = em.createNativeQuery("update course set last_updated_date=sysdate");
        int noOfROwsUpdated = query.executeUpdate();

        //then
        assertThat(noOfROwsUpdated).isEqualTo(4);
    }

    private void syncDB() {
        em.flush();
        em.clear();
    }
}