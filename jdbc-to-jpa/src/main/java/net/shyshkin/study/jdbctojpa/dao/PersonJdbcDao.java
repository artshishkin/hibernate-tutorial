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

    //select * from person;
    public List<Person> findAllMy() {

        return jdbcTemplate.query("select * from person", (rs, rowNum) -> Person.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .location(rs.getString("location"))
                .birthDate(rs.getTimestamp("birth_date").toLocalDateTime())
                .build());
    }

}
