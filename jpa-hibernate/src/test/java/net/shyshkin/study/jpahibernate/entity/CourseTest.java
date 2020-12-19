package net.shyshkin.study.jpahibernate.entity;

import org.assertj.core.api.ThrowableAssert;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
}