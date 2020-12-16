package net.shyshkin.study.jdbctojpa.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.jdbctojpa.domain.Person;
import net.shyshkin.study.jdbctojpa.jdbc.PersonJdbcDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!jdbcDaoSpringBootTest")
public class DataJdbcInit implements CommandLineRunner {

    private final PersonJdbcDao personJdbcDao;

    @Override
    public void run(String... args) throws Exception {
        personJdbcDao.findAll().forEach(person -> log.debug("{}", person));
        personJdbcDao.insertNew(Person.builder()
                .id(10003)
                .name("Nazar")
                .location("NY")
                .birthDate(LocalDateTime.now())
                .build());
    }
}
