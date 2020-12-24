create sequence hibernate_sequence start with 1 increment by 1;

    create table course (
       id bigint not null,
        created_date timestamp,
        last_updated_date timestamp,
        name varchar(255) not null,
        primary key (id)
    );

    create table passport (
       id bigint not null,
        number varchar(255) not null,
        primary key (id)
    );

    create table review (
       id bigint not null,
        description varchar(255),
        rating varchar(255) not null,
        course_id bigint,
        primary key (id)
    );

    create table student (
       id bigint not null,
        name varchar(255) not null,
        passport_id bigint,
        primary key (id)
    );

    create table student_course (
       student_id bigint not null,
        course_id bigint not null
    );

    alter table review 
       add constraint FKprox8elgnr8u5wrq1983degk 
       foreign key (course_id) 
       references course;

    alter table student 
       add constraint FK6i2dofwfuu97njtfprqv68pib 
       foreign key (passport_id) 
       references passport;

    alter table student_course 
       add constraint FKejrkh4gv8iqgmspsanaji90ws 
       foreign key (course_id) 
       references course;

    alter table student_course 
       add constraint FKq7yw2wg9wlt2cnj480hcdn6dq 
       foreign key (student_id) 
       references student;
