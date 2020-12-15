package net.shyshkin.study.jdbctojpa.dao;

import net.shyshkin.study.jdbctojpa.domain.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@ComponentScan
class PersonJdbcDaoJdbcTest {

    @Autowired
    PersonJdbcDao personJdbcDao;

    @Test
    void findAll() {
        //when
        List<Person> personList = personJdbcDao.findAll();

        //then
        assertAll(
                () -> assertThat(personList).hasSize(2),
                () -> assertThat(personList.get(0).getName()).isEqualTo("Art"),
                () -> assertThat(personList.get(1).getName()).isEqualTo("Kate"),
                () -> assertThat(personList.get(0)).hasNoNullFieldsOrProperties()
        );

    }

    @Test
    void findById() {
        //given
        int id = 10001;

        //when
        Person person = personJdbcDao.findById(id);

        //then
        assertThat(person).isNotNull();
        assertThat(person).hasNoNullFieldsOrProperties();
        assertThat(person.getId()).isEqualTo(id);
        assertThat(person.getName()).isEqualTo("Art");

    }

    @Test
    void deleteById() {
        //given
        int id = 10001;

        //when
        int count = personJdbcDao.deleteById(id);

        //then
        assertThat(count).isEqualTo(1);

    }

    @Test
    void count() {
        //when
        Integer count = personJdbcDao.count();

        //then
        assertThat(count).isEqualTo(2);
    }

    @Test
    void insertNew() {
        //given
        Person person = Person.builder()
                .id(10003)
                .name("Nazar")
                .location("NY")
                .birthDate(LocalDateTime.now())
                .build();

        //when
        int insertCount = personJdbcDao.insertNew(person);

        //then
        assertThat(insertCount).isEqualTo(1);
        assertThat(personJdbcDao.count()).isEqualTo(3);


    }

    @Test
    void updatePerson() {
        //given
        Person person = Person.builder()
                .id(10001)
                .name("Arina")
                .location("Paris")
                .birthDate(LocalDateTime.now())
                .build();

        //when
        int insertCount = personJdbcDao.updatePerson(person);

        //then
        assertThat(insertCount).isEqualTo(1);
        Person updatedPerson = personJdbcDao.findById(10001);
        assertThat(updatedPerson).isEqualToIgnoringGivenFields(person, "birthDate");
        assertThat(updatedPerson.getBirthDate()).isEqualToIgnoringNanos(person.getBirthDate());
    }

//    @TestConfiguration
//    static class Config {
//
//        @Bean
//        public PersonJdbcDao personJdbcDao(JdbcTemplate jdbcTemplate) {
//            return new PersonJdbcDao(jdbcTemplate);
//        }
//    }

}