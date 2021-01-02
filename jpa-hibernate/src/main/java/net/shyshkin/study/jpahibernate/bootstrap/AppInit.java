package net.shyshkin.study.jpahibernate.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.jpahibernate.entity.*;
import net.shyshkin.study.jpahibernate.repository.CourseRepository;
import net.shyshkin.study.jpahibernate.repository.EmployeeRepository;
import net.shyshkin.study.jpahibernate.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!spring_boot_test")
public class AppInit implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) throws Exception {
        Employee art = new PartTimeEmployee("Art Part", BigDecimal.valueOf(12.99));
        Employee kate = new FullTimeEmployee("Kate Full", BigDecimal.valueOf(999.99));
        List.of(art, kate).forEach(employeeRepository::insert);

        Student student = studentRepository.findById(20001L);
        student.setAddress(new Address("Line 1", "Line 2", "Kyiv"));
        studentRepository.save(student);
    }
}
