package net.shyshkin.study.jdbctojpa.jpa;

import net.shyshkin.study.jdbctojpa.domain.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDateTime;

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

    @Test
    void createNewPerson() {
        //given
        Person person = Person.builder()
                .name("Tanya")
                .location("Kyiv")
                .birthDate(LocalDateTime.now())
                .build();

        //when
        Person insertedPerson = personRepository.insertNew(person);

        //then
        assertThat(insertedPerson).isEqualToIgnoringGivenFields(person, "id");
        assertThat(insertedPerson).hasNoNullFieldsOrProperties();
        assertThat(personRepository.findById(insertedPerson.getId()).getName()).isEqualTo("Tanya");
    }

    @Test
    void updatePerson() {
        //given
        Person person = Person.builder()
                .id(10001)
                .name("Tanya")
                .location("Kyiv")
                .birthDate(LocalDateTime.now())
                .build();

        //when
        Person updatePerson = personRepository.updatePerson(person);

        //then
        assertThat(updatePerson).isEqualTo(person);
        assertThat(personRepository.findById(10001).getName()).isEqualTo("Tanya");
    }
}