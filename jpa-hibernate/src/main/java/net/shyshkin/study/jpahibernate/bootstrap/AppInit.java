package net.shyshkin.study.jpahibernate.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.jpahibernate.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppInit implements CommandLineRunner {

    private final StudentRepository studentRepository;

    @Override
    public void run(String... args) throws Exception {
        studentRepository.createStudentWithPassport();
    }
}
