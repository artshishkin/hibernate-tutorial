package net.shyshkin.study.jdbctojpa.dao;

import net.shyshkin.study.jdbctojpa.domain.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class PersonJdbcDaoTest {

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
}