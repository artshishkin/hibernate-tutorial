package net.shyshkin.study.jpahibernate.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Rating rating;

    @Getter
    @Setter
    @ManyToOne
    private Course course;

    @Builder
    public Review(String description, Rating rating) {
        this.description = description;
        this.rating = rating;
    }
}
