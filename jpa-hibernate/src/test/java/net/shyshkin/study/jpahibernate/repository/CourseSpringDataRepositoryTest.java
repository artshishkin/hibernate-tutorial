package net.shyshkin.study.jpahibernate.repository;

import net.shyshkin.study.jpahibernate.entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CourseSpringDataRepositoryTest {

    @Autowired
    CourseSpringDataRepository courseRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void findById_coursePresent() {
        //given
        Long id = 10001L;

        //when
        Course course = courseRepository.findById(id)
                .orElseThrow();

        //then
        assertThat(course)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("name", "Spring Boot");
    }

    @Test
    void findById_courseAbsent() {
        //given
        Long id = 20001L;

        //when
        Optional<Course> courseOptional = courseRepository.findById(id);

        //then
        assertThat(courseOptional.isPresent()).isFalse();
    }

    private void syncDB() {
        testEntityManager.flush();
        testEntityManager.clear();
    }
}