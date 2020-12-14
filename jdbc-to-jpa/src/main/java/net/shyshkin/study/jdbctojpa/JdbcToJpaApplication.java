package net.shyshkin.study.jdbctojpa;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.jdbctojpa.dao.PersonJdbcDao;
import net.shyshkin.study.jdbctojpa.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@Slf4j
@SpringBootApplication
public class JdbcToJpaApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(JdbcToJpaApplication.class, args);
    }

    @Autowired
    PersonJdbcDao personJdbcDao;

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
