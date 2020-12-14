create table person
(
    id         integer not null,
    name       varchar(255),
    location   varchar(255),
    birth_date timestamp,
    primary key (id)
);

insert into person (id, name, location, birth_date)
VALUES (10001, 'Art', 'Kram', sysdate);

insert into person (id, name, location, birth_date)
VALUES (10002, 'Kate', 'Kram', current_timestamp);