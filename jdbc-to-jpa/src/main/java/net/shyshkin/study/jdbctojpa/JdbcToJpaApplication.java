package net.shyshkin.study.jdbctojpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class JdbcToJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(JdbcToJpaApplication.class, args);
    }
}
