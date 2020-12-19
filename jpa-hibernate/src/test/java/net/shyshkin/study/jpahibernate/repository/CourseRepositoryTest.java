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

    private void syncDB() {
        testEntityManager.flush();
        testEntityManager.clear();
    }
}