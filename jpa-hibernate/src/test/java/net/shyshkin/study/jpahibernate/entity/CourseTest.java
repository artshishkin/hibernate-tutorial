package net.shyshkin.study.jpahibernate.entity;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.ThrowableAssert;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@DataJpaTest
class CourseTest {

    @Autowired
    TestEntityManager entityManager;

    @Test
    @DirtiesContext
    void nullableTest() {
        //given
        Course course = new Course();

        //when
        course.setName(null);
        ThrowableAssert.ThrowingCallable persistingEntityWithNullName = () -> entityManager.persist(course);

        //then
        assertThatThrownBy(persistingEntityWithNullName)
                .isExactlyInstanceOf(PersistenceException.class)
                .hasCauseInstanceOf(PropertyValueException.class)
                .hasMessageContaining("not-null property references a null or transient value");
    }

    @Test
    void dateUpdateTest() throws InterruptedException {
        //given
        Course course = new Course("Fine Course");
        Course persistedCourse = entityManager.persist(course);
        Long id = persistedCourse.getId();
        entityManager.flush();

        //when
        Thread.sleep(200);
        course.setName("Updated");
        entityManager.flush();

        //then
        Course courseUpdated = entityManager.find(Course.class, id);
        log.info("{}", courseUpdated);
        assertThat(courseUpdated.getLastUpdatedDate())
                .isAfterOrEqualTo(courseUpdated.getCreatedDate().plus(200, ChronoUnit.MILLIS));
    }

    @Test
    void namedQuery() {
        //given
        EntityManager entityManager = this.entityManager.getEntityManager();

        //when
        TypedQuery<Course> allCourses = entityManager.createNamedQuery("query_all_courses", Course.class);
        List<Course> courseList = allCourses.getResultList();

        //then
        assertThat(courseList)
                .hasSize(2)
                .allSatisfy(course -> log.info("{}", course))
                .allSatisfy(course -> assertThat(course).hasNoNullFieldsOrProperties());
    }
}