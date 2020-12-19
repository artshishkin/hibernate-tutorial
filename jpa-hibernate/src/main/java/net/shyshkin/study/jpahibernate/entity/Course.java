package net.shyshkin.study.jpahibernate.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Course")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter
    @Setter
    @Column(name = "name", nullable = false)
    private String name;

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
