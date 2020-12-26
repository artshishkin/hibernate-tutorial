package net.shyshkin.study.jpahibernate.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@ToString
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FullTimeEmployee extends Employee {

    @Column(nullable = false)
    private BigDecimal salary;

    public FullTimeEmployee(String name, BigDecimal salary) {
        super(name);
        this.salary = salary;
    }
}
