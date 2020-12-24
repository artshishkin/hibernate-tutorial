insert into course (id, name, created_date, last_updated_date)
values (10001, 'Spring Boot', systimestamp, systimestamp);
insert into course (id, name, created_date, last_updated_date)
values (10002, 'Hibernate', sysdate, sysdate);
insert into course (id, name, created_date, last_updated_date)
values (10003, 'Kafka', sysdate, sysdate);

insert into passport (id, number)
values (30001, 'a123456');
insert into passport (id, number)
values (30002, 'b67890');
insert into passport (id, number)
values (30003, 'c321321');

insert into student (id, name, passport_id)
values (20001, 'Art', 30001);
insert into student (id, name, passport_id)
values (20002, 'Kate',30002);
insert into student (id, name, passport_id)
values (20003, 'Arina',30003);

insert into review (id, description, rating, course_id)
values (40001, 'Good','80',10001);
insert into review (id, description, rating, course_id)
values (40002, null,'90',10001);
insert into review (id, description, rating, course_id)
values (40003, 'Bad','20',10002);
insert into review (id, description, rating, course_id)
values (40004, 'Awesome','100',10003);

insert into student_course (student_id, course_id)
values (20001,10001);
insert into student_course (student_id, course_id)
values (20001,10002);
insert into student_course (student_id, course_id)
values (20001,10003);
insert into student_course (student_id, course_id)
values (20002,10001);
insert into student_course (student_id, course_id)
values (20002,10002);
insert into student_course (student_id, course_id)
values (20003,10003);



