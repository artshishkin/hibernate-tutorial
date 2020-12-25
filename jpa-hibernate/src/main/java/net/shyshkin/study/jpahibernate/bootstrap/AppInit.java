package net.shyshkin.study.jpahibernate.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.jpahibernate.repository.CourseRepository;
import net.shyshkin.study.jpahibernate.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!spring_boot_test")
public class AppInit implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Override
    public void run(String... args) throws Exception {
        studentRepository.insertHardcodedStudentAndCourse();
    }
}
