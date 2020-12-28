package net.shyshkin.study.jpahibernate.repository;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.jpahibernate.entity.Course;
import net.shyshkin.study.jpahibernate.entity.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@ComponentScan
class JPQLTest {

    @Autowired
    EntityManager em;

    @Test
    void findAll_basic() {
        //when
//        Query query = em.createQuery("select c from Course c");
        Query query = em.createNamedQuery("query_all_courses");
        List courses = query.getResultList();


        //then
        assertThat(courses).hasSize(4)
                .allSatisfy(course -> log.info("{}", course))
                .allSatisfy(course -> assertThat(course).hasNoNullFieldsOrProperties());
    }

    @Test
    void findAll_typed() {
        //when
//        TypedQuery<Course> typedQuery = em.createQuery("select c from Course c", Course.class);
        TypedQuery<Course> typedQuery = em.createNamedQuery("query_all_courses", Course.class);
        List<Course> courses = typedQuery.getResultList();


        //then
        assertThat(courses)
                .hasSize(4)
                .allSatisfy(course -> log.info("{}", course))
                .allSatisfy(course -> assertThat(course).hasNoNullFieldsOrProperties());
    }

    @Test
    void find_where() {
        //when
//        TypedQuery<Course> typedQuery = em.createNamedQuery("select c from Course c where c.name like '%nate'", Course.class);
        TypedQuery<Course> typedQuery = em.createNamedQuery("query_courses_like_hiberNATE", Course.class)
                .setParameter("namePart", "%nate");
        List<Course> courses = typedQuery.getResultList();


        //then
        assertThat(courses)
                .hasSize(1)
                .allSatisfy(course -> log.info("{}", course))
                .allSatisfy(course ->
                        assertThat(course)
                                .hasNoNullFieldsOrProperties()
                                .hasFieldOrPropertyWithValue("name", "Hibernate"));
    }

    @Test
    void jpql_queries_update() {
        //when
        Query query = em.createQuery("update Course c set c.lastUpdatedDate=current_timestamp");
        int noOfROwsUpdated = query.executeUpdate();

        //then
        assertThat(noOfROwsUpdated).isEqualTo(4);
    }

    private void syncDB() {
        em.flush();
        em.clear();
    }

    @Test
    void jpql_coursesWithoutStudents() {
        //when
        TypedQuery<Course> typedQuery = em.createQuery("select c from Course c where c.students is empty", Course.class);
        List<Course> courseList = typedQuery.getResultList();

        //then
        assertThat(courseList)
                .hasSize(1)
                .allSatisfy(course -> assertThat(course.getStudents()).isEmpty())
                .allMatch(course -> course.getName().equals("AWS Developer"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "select c from Course c where c.students.size >= 2",
            "select c from Course c where size(c.students) >= 2"
    })
    void jpql_coursesWithAtLeast2Students(String queryString) {
        //when
        TypedQuery<Course> typedQuery = em.createQuery(queryString, Course.class);
        List<Course> courseList = typedQuery.getResultList();

        //then
        assertThat(courseList)
                .hasSize(3)
                .allSatisfy(course -> assertThat(course.getStudents()).hasSizeGreaterThanOrEqualTo(2));
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "select c from Course c order by c.students.size",
            "select c from Course c order by size(c.students)"
    })
    void jpql_orderCoursesByNumberOfStudents(String queryString) {
        //when
        TypedQuery<Course> typedQuery = em.createQuery(queryString, Course.class);
        List<Course> courseList = typedQuery.getResultList();

        //then
        assertThat(courseList)
                .hasSize(4)
                .isSortedAccordingTo(Comparator.comparing((course) -> course.getStudents().size()));

        courseList.forEach(course -> log.info("Course `{}` with students count: {}", course, course.getStudents().size()));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "select s from Student s where s.passport.number like '%1234%'"
    })
    void jpql_studentWithPassportLike1234(String queryString) {
        //when
        TypedQuery<Student> typedQuery = em.createQuery(queryString, Student.class);
        List<Student> studentList = typedQuery.getResultList();

        //then
        assertThat(studentList)
                .hasSize(2)
                .allSatisfy(student -> assertThat(student.getPassport().getNumber()).contains("1234"));
        studentList.forEach(student -> log.info("{}", student));
    }

    //like
    //between 100 and 1000
    //is NULL
    //upper, lower, trim, length


    @Test
    @DisplayName("Course join Student will NOT return Rows with Courses without student (INNER JOIN)")
    void join() {
        //given

        //when
        Query query = em.createQuery("select c, s from Course c join c.students s");
        List<Object[]> resultList = query.getResultList();

        //then
        assertThat(resultList)
                .hasSize(7)
                .allMatch(array -> array[0].getClass().isAssignableFrom(Course.class))
                .allMatch(array -> array[1].getClass().isAssignableFrom(Student.class));
    }

    @Test
    @DisplayName("Course left join Student WILL return Rows with Courses without student (LEFT OUTER JOIN)")
    void left_outer_join() {
        //given

        //when
        Query query = em.createQuery("select c, s from Course c left join c.students s");
        List<Object[]> resultList = query.getResultList();

        //then
        assertThat(resultList)
                .hasSize(8)
                .allMatch(array -> array[0].getClass().isAssignableFrom(Course.class))
                .allMatch(array -> array[1] == null || array[1].getClass().isAssignableFrom(Student.class));
    }

    //CROSS JOIN => select c, s from Course c, Student s
    //3 and 4 => 3 * 4 = 12 Rows

    @Test
    @DisplayName("Course cross join Student will return ALL THE COMBINATIONS")
    void cross_join() {
        //given

        //when
        Query query = em.createQuery("select c, s from Course c, Student s");
        List<Object[]> resultList = query.getResultList();

        //then
        assertThat(resultList)
                .hasSize(12)
                .allMatch(array -> array[1] == null || array[0].getClass().isAssignableFrom(Course.class))
                .allMatch(array -> array[1] == null || array[1].getClass().isAssignableFrom(Student.class));
    }

    @Test
    @DisplayName("Choose Courses with Students who have Passport with numbers like 1234")
    void complex_join_query() {
       //when
        TypedQuery<Course> typedQuery = em.createQuery("select distinct c from Course c join c.students s where s.passport.number like '%1234%'", Course.class);
        List<Course> resultList = typedQuery.getResultList();

        //then
        resultList.forEach(course -> log.info("{}", course));
        assertThat(resultList)
                .hasSize(3)
                .allSatisfy(
                        course -> assertThat(course.getStudents())
                                .anySatisfy(
                                        student -> assertThat(student.getPassport().getNumber())
                                                .contains("1234")));
    }
}