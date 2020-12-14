package net.shyshkin.study.jdbctojpa.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {

    private Integer id;
    private String name;
    private String location;
    private LocalDateTime birthDate;
}
