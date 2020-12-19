package net.shyshkin.study.jpahibernate.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.jpahibernate.repository.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseInit implements CommandLineRunner {

    private final CourseRepository courseRepository;

    @Override
    public void run(String... args) throws Exception {
        log.debug("Start bootstrap course repository");
        courseRepository.playWithEntityManagerDetach();
        log.debug("Finish bootstrap course repository");
    }
}
