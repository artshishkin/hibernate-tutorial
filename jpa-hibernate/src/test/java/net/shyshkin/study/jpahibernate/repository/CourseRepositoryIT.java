package net.shyshkin.study.jpahibernate.repository;

import net.shyshkin.study.jpahibernate.entity.Course;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

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
}