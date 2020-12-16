package net.shyshkin.study.jdbctojpa.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.jdbctojpa.domain.Person;
import net.shyshkin.study.jdbctojpa.jpa.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!jdbcDaoSpringBootTest")
public class DataJpaInit implements CommandLineRunner {

    private final PersonRepository personRepository;

    @Override
    public void run(String... args) throws Exception {
        log.debug("Inserting new person through JPA");
        personRepository.insertNew(
                Person.builder()
                        .name("Arina")
                        .location("Lviv")
                        .birthDate(LocalDateTime.now())
                        .build());

        log.debug("Updating person through JPA");
        personRepository.updatePerson(
                Person.builder()
                        .id(10002)
                        .name("Katerine")
                        .location("Paris")
                        .birthDate(LocalDateTime.now())
                        .build());
    }
}
