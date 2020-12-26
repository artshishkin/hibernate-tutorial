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
public class PartTimeEmployee extends Employee {

    @Column(nullable = true)
    private BigDecimal hourlyWage;

    public PartTimeEmployee(String name, BigDecimal hourlyWage) {
        super(name);
        this.hourlyWage = hourlyWage;
    }
}
