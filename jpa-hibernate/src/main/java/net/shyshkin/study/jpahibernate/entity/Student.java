package net.shyshkin.study.jpahibernate.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

@Entity
@Table
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NamedQuery(name = "findStudentByName", query = "select s from Student s where s.name=:name")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Student {

    @Id
    @GeneratedValue
    @Getter
    @EqualsAndHashCode.Include
    private Long id;

    @Getter
    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    private Passport passport;

    public Student(String name) {
        this.name = name;
    }

    @ToString.Exclude
    @ManyToMany(mappedBy = "students")
    private final Set<Course> courses = new HashSet<>();

    public Set<Course> getCourses() {
        return courses;
//        return unmodifiableSet(courses);
    }

    public Student addCourse(Course course) {
        courses.add(course);
        course.getStudents().add(this);
        return this;
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        course.getStudents().remove(this);
    }

//    @PreRemove
    public void removeStudentFromCourses() {
        LinkedList<Course> courseQueue = new LinkedList<>(courses);

        Course courseToDelete;
        while ((courseToDelete = courseQueue.pollFirst()) != null)
            courseToDelete.removeStudent(this);
    }
}
