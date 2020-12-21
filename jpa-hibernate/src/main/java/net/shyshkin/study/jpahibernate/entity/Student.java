package net.shyshkin.study.jpahibernate.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne
    @Getter @Setter
    private Passport passport;

    public Student(String name) {
        this.name = name;
    }
}
