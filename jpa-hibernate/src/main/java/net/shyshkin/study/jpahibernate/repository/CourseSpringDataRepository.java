package net.shyshkin.study.jpahibernate.repository;

import net.shyshkin.study.jpahibernate.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CourseSpringDataRepository extends JpaRepository<Course, Long> {

    List<Course> findByName(String name);

    long countByName(String name);

    List<Course> findByNameContainingAndAndCreatedDateAfter(String namePart, LocalDateTime date);

    List<Course> findByNameContainingOrderByNameDesc(String namePart);

    void deleteByName(String name);

    @Query("select distinct c from Course c join c.students s where s.passport.number like '%1234%'")
    List<Course> findCoursesThatHaveStudentsWithPassportNumbersLike1234();

    @Query("select distinct c from Course c join c.students s where s.passport.number like :numberPattern")
    List<Course> findCoursesThatHaveStudentsWithPassportNumbersLike(String numberPattern);

    @Query(value = "select * from course where name like :namePart", nativeQuery = true)
    List<Course> findNativeCoursesWithNamesLike(String namePart);

    @Query(name = "query_courses_like_hiberNATE")
    List<Course> findCoursesWithNamesLikeUsingNamedQuery(String namePart);

}
