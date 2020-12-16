package net.shyshkin.study.jdbctojpa.jpa;

import net.shyshkin.study.jdbctojpa.domain.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ComponentScan
class PersonRepositoryTest {

    @Autowired
    PersonRepository personRepository;

    @Test
    void findById() {
        //given
        int id = 10001;

        //when
        Person person = personRepository.findById(id);

        //then
        assertThat(person).isNotNull();
        assertThat(person.getId()).isEqualTo(id);
        assertThat(person.getName()).isEqualTo("Art");
    }
}