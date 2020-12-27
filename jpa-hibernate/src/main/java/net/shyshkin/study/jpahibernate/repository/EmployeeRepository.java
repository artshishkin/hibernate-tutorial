package net.shyshkin.study.jpahibernate.repository;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.jpahibernate.entity.Employee;
import net.shyshkin.study.jpahibernate.entity.FullTimeEmployee;
import net.shyshkin.study.jpahibernate.entity.PartTimeEmployee;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployeeRepository {

    private final EntityManager em;

    //insert employee
    @Transactional
    public Employee insert(Employee employee) {
        em.persist(employee);
        return employee;
    }

    //findAll
    public List<Employee> findAll() {
        List<FullTimeEmployee> fullTimeEmployeeList = em
                .createQuery("select e from FullTimeEmployee e", FullTimeEmployee.class)
                .getResultList();
        List<PartTimeEmployee> partTimeEmployeeList = em
                .createQuery("select e from PartTimeEmployee e", PartTimeEmployee.class)
                .getResultList();
        List<Employee> employeeList = new ArrayList<>();
        employeeList.addAll(fullTimeEmployeeList);
        employeeList.addAll(partTimeEmployeeList);
        return employeeList;
    }

}
