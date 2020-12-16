package net.shyshkin.study.jdbctojpa.jpa;

import net.shyshkin.study.jdbctojpa.domain.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ComponentScan
class PersonRepositoryTest {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void findAll() {

        //when
        List<Person> personList = personRepository.findAll();

        //then
        assertThat(personList).isNotNull().hasSize(2);

    }

    @Test
    void findAllNamed() {

        //when
        List<Person> personList = personRepository.findAllNamed();

        //then
        assertThat(personList).isNotNull().hasSize(2);

    }

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
        syncDB();
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
        syncDB();
        assertThat(updatePerson).isEqualTo(person);
        assertThat(personRepository.findById(10001).getName()).isEqualTo("Tanya");
    }

    @Test
    void deleteById() {

        //when
        personRepository.deleteById(10001);

        //then
        syncDB();
        assertThat(personRepository.findById(10001)).isNull();
    }

    private void syncDB() {
        // forces synchronization to DB
        testEntityManager.flush();
        // clears persistence context
        // all entities are now detached and can be fetched again
        testEntityManager.clear();
    }
}