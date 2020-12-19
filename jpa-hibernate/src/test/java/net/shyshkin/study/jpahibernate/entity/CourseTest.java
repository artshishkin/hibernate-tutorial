package net.shyshkin.study.jpahibernate.entity;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.ThrowableAssert;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.PersistenceException;
import java.time.temporal.ChronoUnit;

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
}