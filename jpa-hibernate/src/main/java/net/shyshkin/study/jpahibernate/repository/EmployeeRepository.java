package net.shyshkin.study.jpahibernate.repository;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.jpahibernate.entity.Employee;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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
        TypedQuery<Employee> typedQuery = em.createQuery("select e from Employee e", Employee.class);
        return typedQuery.getResultList();
    }

}
