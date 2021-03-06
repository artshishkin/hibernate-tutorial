package net.shyshkin.study.jpahibernate.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.unmodifiableList;

@Slf4j
@Entity
@Table(name = "Course")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NamedQueries({
        @NamedQuery(name = "query_all_courses", query = "select c from Course c"),
        @NamedQuery(name = "query_courses_like_hiberNATE", query = "select c from Course c where c.name like :namePart")})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Cacheable
@SQLDelete(sql = "update COURSE set is_deleted=true where ID=?")
@Where(clause = "is_deleted = false")
public class Course {

    @Id
    @GeneratedValue
    @Getter
    @EqualsAndHashCode.Include
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
    @ManyToMany
    @JoinTable(name = "STUDENT_COURSE",
            joinColumns = @JoinColumn(name = "COURSE_ID"),
            inverseJoinColumns = @JoinColumn(name = "STUDENT_ID")
    )
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

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;

    public Course(String name) {
        this.name = name;
    }

    @PreRemove
    private void preRemove() {
        log.info("Setting isDeleted to true");
        this.isDeleted = true;
    }

}
