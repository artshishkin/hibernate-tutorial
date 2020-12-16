package net.shyshkin.study.jdbctojpa.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@NamedQuery(name = "find_all_persons", query = "select p from Person p")
public class Person {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String location;
    private LocalDateTime birthDate;
}
