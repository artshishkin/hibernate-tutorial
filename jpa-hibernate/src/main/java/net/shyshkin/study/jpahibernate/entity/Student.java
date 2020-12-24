package net.shyshkin.study.jpahibernate.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

@Entity
@Table
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {

    @Id
    @GeneratedValue
    @Getter
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

    @ManyToMany
    private final List<Course> courses = new ArrayList<>();

    public List<Course> getCourses() {
        return unmodifiableList(courses);
    }

    public Student addCourse(Course course) {
        courses.add(course);
        return this;
    }

    public void removeCourse(Course course) {
        courses.remove(course);
    }
}
