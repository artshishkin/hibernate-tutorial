package net.shyshkin.study.jpahibernate.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
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
