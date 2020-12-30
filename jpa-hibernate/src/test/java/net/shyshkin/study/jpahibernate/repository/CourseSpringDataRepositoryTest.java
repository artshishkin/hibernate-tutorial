package net.shyshkin.study.jpahibernate.repository;

import net.shyshkin.study.jpahibernate.entity.Course;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;

import java.util.List;
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

    @Test
    @DisplayName("Test is in a transaction so second courseRepository.save is not necessary")
    void saveAndUpdate() {
        //given
        Course course = new Course("Microservices in 50 Steps");

        //when
        courseRepository.save(course);

        course.setName("Micro 2021");
//        courseRepository.save(course);

        //then
        long count = courseRepository.count(Example.of(new Course("Micro 2021")));
        assertThat(count).isEqualTo(1);
    }

    @Test
    void findAll() {
        //when
        List<Course> courseList = courseRepository.findAll();

        //then
        assertThat(courseList).hasSizeGreaterThan(2);
    }

    @Test
    void count() {
        //when
        long count = courseRepository.count();

        //then
        assertThat(count).isGreaterThan(2);
    }

    private void syncDB() {
        testEntityManager.flush();
        testEntityManager.clear();
    }
}