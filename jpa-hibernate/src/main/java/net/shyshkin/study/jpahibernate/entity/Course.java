package net.shyshkin.study.jpahibernate.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.Collections.unmodifiableList;

@Entity
@Table(name = "Course")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NamedQueries({
        @NamedQuery(name = "query_all_courses", query = "select c from Course c"),
        @NamedQuery(name = "query_courses_like_hiberNATE", query = "select c from Course c where c.name like :namePart")})
public class Course {

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter
    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "course", cascade = {CascadeType.REMOVE}/*,fetch = FetchType.EAGER*/)
    private final List<Review> reviews = new ArrayList<>();

    public void addReview(Review review) {
        review.setCourse(this);
        reviews.add(review);
    }

    public void removeReview(Review review) {
        reviews.remove(review);
    }

    public List<Review> getReviews() {
        return unmodifiableList(reviews);
    }

    @ToString.Exclude
    @ManyToMany(mappedBy = "courses")
    private final Set<Student> students = new HashSet<>();

    public Set<Student> getStudents() {
        return students;
//        return unmodifiableSet(students);
    }

    public Course addStudent(Student student) {
        students.add(student);
        student.getCourses().add(this);
        return this;
    }

    public void removeStudent(Student student) {
        students.remove(student);
        student.getCourses().remove(this);
    }

    @Getter
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Getter
    @Setter
    @UpdateTimestamp
    private LocalDateTime lastUpdatedDate;

    public Course(String name) {
        this.name = name;
    }

    @PreRemove
    public void removeCourseFromStudents() {
        LinkedList<Student> studentQueue = new LinkedList<>(students);

        Student studentToDelete;
        while ((studentToDelete = studentQueue.pollFirst()) != null)
            studentToDelete.removeCourse(this);
    }

}
