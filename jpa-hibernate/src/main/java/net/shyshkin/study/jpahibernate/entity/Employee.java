package net.shyshkin.study.jpahibernate.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Employee {

    @Id
    @GeneratedValue
    @Getter
    @EqualsAndHashCode.Include
    private Long id;

    @Getter
    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    public Employee(String name) {
        this.name = name;
    }
}
