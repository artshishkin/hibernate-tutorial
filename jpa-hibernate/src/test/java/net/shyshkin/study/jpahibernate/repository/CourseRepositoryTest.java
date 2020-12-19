package net.shyshkin.study.jpahibernate.repository;

import net.shyshkin.study.jpahibernate.entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;

import static org.assertj.core.api.Assertions.assertThat;

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
}