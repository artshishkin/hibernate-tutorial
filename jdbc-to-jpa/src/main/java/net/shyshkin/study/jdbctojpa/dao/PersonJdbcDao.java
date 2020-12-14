package net.shyshkin.study.jdbctojpa.dao;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.jdbctojpa.domain.Person;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PersonJdbcDao {

    private final JdbcTemplate jdbcTemplate;

    //select * from person;
    public List<Person> findAll() {

        return jdbcTemplate.query("select * from person", BeanPropertyRowMapper.newInstance(Person.class));
    }

    public Person findById(int id) {
        return jdbcTemplate.queryForObject("select * from person where id=?",
                BeanPropertyRowMapper.newInstance(Person.class), id);
    }

    public int deleteById(int id) {
        return jdbcTemplate.update("delete from person where id=?", id);
    }

    public Integer count() {
        return jdbcTemplate.queryForObject("select count(*) from PERSON", Integer.class);
    }

    public int insertNew(Person person) {
        return jdbcTemplate.update("insert into person (id, name, location, birth_date) values (?,?,?,?)",
                person.getId(), person.getName(), person.getLocation(), person.getBirthDate());
    }

    public int updatePerson(Person person) {
        return jdbcTemplate.update("update person set name=?, location=?, birth_date=? where ID=?",
                person.getName(), person.getLocation(), person.getBirthDate(), person.getId());
    }


}
