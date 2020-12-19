package net.shyshkin.study.jpahibernate.repository;

import net.shyshkin.study.jpahibernate.entity.Course;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@ComponentScan
class CourseRepositoryTest {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void findById() {
        //given
        Long id = 10001L;

        //when
        Course course = courseRepository.findById(id);

        //then
        assertThat(course)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("name", "Spring Boot");
    }

    @Test
    void deleteById() {
        //given
        Long id = 10001L;

        //when
        courseRepository.deleteById(id);

        //then
        syncDB();
        assertThat(courseRepository.findById(id)).isNull();
    }

    @Test
    void saveInsert() {
        //given
        Course course = new Course("New Course");

        //when
        Course savedCourse = courseRepository.save(course);

        //then
        syncDB();
        assertThat(savedCourse)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("name", "New Course");
    }

    @Test
    void saveUpdate() {
        //given
        Course course = courseRepository.findById(10001L);
        course.setName("Updated Course");

        //when
        Course savedCourse = courseRepository.save(course);

        //then
        syncDB();
        assertThat(savedCourse)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("name", "Updated Course")
                .hasFieldOrPropertyWithValue("id", 10001L);
    }


    private void syncDB() {
        testEntityManager.flush();
        testEntityManager.clear();
    }

    @Test
    @DisplayName("Detaching removes the given entity from the persistence context, causing a managed entity to become detached. Unflushed changes made to the entity if any (including removal of the entity), will not be synchronized to the database. Entities which previously referenced the detached entity will continue to reference it.")
    void playWithEntityManagerDetach() {
        courseRepository.playWithEntityManagerDetach();
        syncDB();
        assertAll(
                () -> assertThat(countCoursesByName("Play course 1")).isEqualTo(0L),
                () -> assertThat(countCoursesByName("Play course 2")).isEqualTo(1L),
                () -> assertThat(countCoursesByName("Updated course 1")).isEqualTo(1L),
                () -> assertThat(countCoursesByName("Updated course 2")).isEqualTo(0L)
        );
    }

    @Test
    @DisplayName("After Clearing the persistence context all managed entities become detached. Changes made to entities that have not been flushed to the database will not be persisted.")
    void playWithEntityManagerClear() {
        courseRepository.playWithEntityManagerClear();
        syncDB();
        assertAll(
                () -> assertThat(countCoursesByName("Play course 1")).isEqualTo(1L),
                () -> assertThat(countCoursesByName("Play course 2")).isEqualTo(1L),
                () -> assertThat(countCoursesByName("Updated course 1")).isEqualTo(0L),
                () -> assertThat(countCoursesByName("Updated course 2")).isEqualTo(0L)
        );
    }

    @Test
    @DisplayName("Refresh the state of the instance from the database, overwriting changes made to the entity, if any.")
    void playWithEntityManagerRefresh() {
        courseRepository.playWithEntityManagerRefresh();
        syncDB();
        assertAll(
                () -> assertThat(countCoursesByName("Play course 1")).isEqualTo(0L),
                () -> assertThat(countCoursesByName("Play course 2")).isEqualTo(1L),
                () -> assertThat(countCoursesByName("Updated course 1")).isEqualTo(1L),
                () -> assertThat(countCoursesByName("Updated course 2")).isEqualTo(0L)
        );
    }

    private Long countCoursesByName(String name) {
        EntityManager entityManager = testEntityManager.getEntityManager();
        TypedQuery<Long> typedQuery = entityManager.createQuery("select count(c) from Course c where c.name = '" + name + "'", Long.class);

        return typedQuery.getSingleResult();
    }
}