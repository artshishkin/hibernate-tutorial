package net.shyshkin.study.jpahibernate.repository;

import net.shyshkin.study.jpahibernate.entity.Employee;
import net.shyshkin.study.jpahibernate.entity.FullTimeEmployee;
import net.shyshkin.study.jpahibernate.entity.PartTimeEmployee;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ComponentScan
class EmployeeRepositoryTest {

    @Autowired
    TestEntityManager tem;

    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    void insertEmployee() {
        //given
        String name = "Art Part";
        BigDecimal hourlyWage = BigDecimal.valueOf(12.99);

        Employee art = new PartTimeEmployee(name, hourlyWage);

        //when
        Employee artSaved = employeeRepository.insert(art);

        //then
        syncDB();
        assertThat(artSaved.getId()).isNotNull();
        assertThat(artSaved.getName()).isEqualTo(name);
        assertThat(artSaved)
                .isInstanceOf(PartTimeEmployee.class)
                .asInstanceOf(InstanceOfAssertFactories.type(PartTimeEmployee.class))
                .hasFieldOrPropertyWithValue("hourlyWage", hourlyWage);

        PartTimeEmployee employeeFromDB = tem.find(PartTimeEmployee.class, artSaved.getId());
        assertThat(employeeFromDB.getName()).isEqualTo(name);
        assertThat(employeeFromDB.getHourlyWage()).isEqualTo(hourlyWage);
    }

    private void syncDB() {
        tem.flush();
        tem.clear();
    }

    @Test
    void findAll() {
        //given
        Employee art = new PartTimeEmployee("Art Part", BigDecimal.valueOf(12.99));
        Employee kate = new FullTimeEmployee("Kate Full", BigDecimal.valueOf(999.99));
        tem.persistAndFlush(art);
        tem.persistAndFlush(kate);

        //when
        List<Employee> employees = employeeRepository.findAll();

        //then
        assertThat(employees)
                .hasSize(2);
    }
}