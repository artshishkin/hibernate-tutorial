drop table if exists person;

create table person
(
    id         integer not null,
    name       varchar(255),
    location   varchar(255),
    birth_date timestamp,
    primary key (id)
);