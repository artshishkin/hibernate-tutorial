package net.shyshkin.study.jpahibernate.repository;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.jpahibernate.entity.Course;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Slf4j
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
    void sort() {
        //given
        Sort sort = Sort.by(Sort.Direction.DESC, "name").and(Sort.by("createdDate"));

        //when
        List<Course> courseList = courseRepository.findAll(sort);

        //then
        assertThat(courseList)
                .hasSizeGreaterThan(2)
                .isSortedAccordingTo(comparing(Course::getName, reverseOrder()));
    }

    @Test
    void pagination() {
        //given
        Pageable pageable = PageRequest.of(1, 2);

        //when
        Page<Course> page = courseRepository.findAll(pageable);

        //then
        assertAll(
                () -> assertThat(page.getTotalElements()).isGreaterThan(2),
                () -> assertThat(page.getContent()).hasSize(2),
                () -> assertThat(page.getNumber()).isEqualTo(1)
        );
    }

    @Test
    void paginationPreviousPage() {
        //given
        Pageable pageable = PageRequest.of(1, 2);

        //when
        Page<Course> page = courseRepository.findAll(pageable);

        //then
        Pageable firstPageable = page.previousPageable();

        assertAll(
                () -> assertThat(firstPageable.getPageSize()).isEqualTo(2),
                () -> assertThat(firstPageable.hasPrevious()).isFalse(),
                () -> assertThat(firstPageable.getPageNumber()).isEqualTo(0)
        );

        Page<Course> firstPage = courseRepository.findAll(firstPageable);
        assertAll(
                () -> assertThat(firstPage.getTotalElements()).isGreaterThan(2),
                () -> assertThat(firstPage.getContent()).hasSize(2),
                () -> assertThat(firstPage.getNumber()).isEqualTo(0)
        );
    }

    @Test
    void count() {
        //when
        long count = courseRepository.count();

        //then
        assertThat(count).isGreaterThan(2);
    }

    @Test
    void findByName() {
        //given
        String name = "Kafka";

        //when
        List<Course> courses = courseRepository.findByName(name);

        //then
        assertThat(courses)
                .hasSize(1)
                .allSatisfy(course -> assertThat(course.getName()).isEqualTo(name));
    }

    @Test
    void countByName() {
        //given
        String name = "Kafka";

        //when
        long count = courseRepository.countByName(name);

        //then
        assertThat(count).isEqualTo(1);
    }

    @Test
    void findByNameContainingAndAndCreatedDateAfter() {
        //given
        String namePart = "nate";
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);

        //when
        List<Course> courseList = courseRepository.findByNameContainingAndAndCreatedDateAfter(namePart, startTime);

        //then
        assertThat(courseList)
                .hasSize(1)
                .allSatisfy(course -> assertThat(course.getName()).containsIgnoringCase("nate"));
    }

    @Test
    void findByNameContainingOrderByNameDesc() {
        //given
        String namePart = "r";

        //when
        List<Course> courseList = courseRepository.findByNameContainingOrderByNameDesc(namePart);

        //then
        assertThat(courseList)
                .hasSize(3)
                .isSortedAccordingTo(comparing(Course::getName, reverseOrder()));
    }

    @Test
    void deleteByName() {
        //given
        String name = "Kafka";
        assertThat(courseRepository.countByName(name)).isEqualTo(1);

        //when
        courseRepository.deleteByName(name);

        //then
        assertThat(courseRepository.countByName(name)).isEqualTo(0);
    }

    @Test
    void findCoursesThatHaveStudentsWithPassportNumbersLike1234() {
        //given

        //when
        List<Course> courseList = courseRepository.findCoursesThatHaveStudentsWithPassportNumbersLike1234();

        //then
        courseList.forEach(course -> log.info("{}", course));
        assertThat(courseList)
                .hasSize(3)
                .allSatisfy(
                        course -> assertThat(course.getStudents())
                                .anySatisfy(
                                        student -> assertThat(student.getPassport().getNumber())
                                                .contains("1234")));
    }

    @Test
    void findCoursesThatHaveStudentsWithPassportNumbersLike() {
        //given
        String numberPattern = "%1234%";

        //when
        List<Course> courseList = courseRepository.findCoursesThatHaveStudentsWithPassportNumbersLike(numberPattern);

        //then
        courseList.forEach(course -> log.info("{}", course));
        assertThat(courseList)
                .hasSize(3)
                .allSatisfy(
                        course -> assertThat(course.getStudents())
                                .anySatisfy(
                                        student -> assertThat(student.getPassport().getNumber())
                                                .contains("1234")));
    }

    @Test
    void findNativeCoursesWithNamesLike() {
        //given
        String namePart = "%nate%";

        //when
        List<Course> courseList = courseRepository.findNativeCoursesWithNamesLike(namePart);

        //then
        courseList.forEach(course -> log.info("{}", course));
        assertThat(courseList)
                .hasSize(1)
                .allSatisfy(course -> assertThat(course.getName()).containsIgnoringCase("nate"));
    }

    private void syncDB() {
        testEntityManager.flush();
        testEntityManager.clear();
    }
}