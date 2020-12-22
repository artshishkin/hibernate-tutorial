package net.shyshkin.study.jpahibernate.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany
    @JoinColumn(name = "course_id")
    private List<Review> reviews = new ArrayList<>();

    public void addReview(Review review) {
        reviews.add(review);
    }

    public void removeReview(Review review) {
        reviews.remove(review);
    }

    public List<Review> getReviews() {
        return unmodifiableList(reviews);
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
}
