package net.shyshkin.study.jpahibernate.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Passport {

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter
    @Setter
    @Column( nullable = false)
    private String number;

    @Getter @Setter
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "passport")
    private Student student;

    public Passport(String number) {
        this.number = number;
    }
}
