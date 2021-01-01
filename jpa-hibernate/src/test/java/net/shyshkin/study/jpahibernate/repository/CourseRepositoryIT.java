package net.shyshkin.study.jpahibernate.repository;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.jpahibernate.entity.Course;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ComponentScan
class CourseRepositoryIT {

    @Autowired
    CourseRepository courseRepository;

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
    @DisplayName("2 identical calls OUT of Transaction does NOT use First Level Caching (2 calls)")
    void findById_withoutCaching() {
        //given
        Long id = 10001L;

        //when
        Course course1 = courseRepository.findById(id);
        log.info("course1 was retrieved: {}", course1);
        Course course2 = courseRepository.findById(id);
        log.info("course2 was retrieved: {}", course2);

        //then
        assertThat(course1)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("name", "Spring Boot");
    }

    @Test
    @DisplayName("2 identical calls withing Transaction switch ON First Level Caching (1 call)")
    @Transactional
    void findById_withCaching() {
        //given
        Long id = 10001L;

        //when
        Course course1 = courseRepository.findById(id);
        log.info("course1 was retrieved: {}", course1);
        Course course2 = courseRepository.findById(id);
        log.info("course2 was retrieved: {}", course2);

        //then
        assertThat(course1)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("name", "Spring Boot");
    }

    @RepeatedTest(3)
    @DirtiesContext
    @DisplayName("After executing deleteById() we should clean dirty context")
    void deleteById() {
        //given
        Long id = 10001L;

        //when
        courseRepository.deleteById(id);

        //then
        assertThat(courseRepository.findById(id)).isNull();
    }

    @Test
    @DirtiesContext
    void saveInsert() {
        //given
        Course course = new Course("New Course");

        //when
        Course savedCourse = courseRepository.save(course);

        //then
        assertThat(savedCourse)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("name", "New Course");
    }

    @Test
    @DirtiesContext
    void saveUpdate() {
        //given
        Course course = courseRepository.findById(10001L);
        course.setName("Updated Course");

        //when
        Course savedCourse = courseRepository.save(course);

        //then
        assertThat(savedCourse)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("name", "Updated Course")
                .hasFieldOrPropertyWithValue("id", 10001L);
    }


}