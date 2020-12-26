# Master Hibernate and JPA with Spring Boot in 100 Steps

## Tutorial from in28Minutes (Udemy)

#####  N plus One Problem

[N+1 query problem with JPA and Hibernate](https://vladmihalcea.com/n-plus-1-query-problem/)

Unlike the default fetch plan, which you are using when calling the find method of the EntityManager, 
a JPQL or Criteria API query defines an explicit plan that Hibernate cannot change by injecting a JOIN FETCH automatically. 
So, you need to do it manually.

-  `JOIN FETCH` rules

#####  92. Step 38 - ManyToMany Mapping - Insert Data and Write Join Query

```sql
SELECT * FROM COURSE, STUDENT_COURSE, STUDENT WHERE COURSE_ID = COURSE.ID AND STUDENT_ID = STUDENT.ID;
```

```sql
SELECT * FROM COURSE JOIN STUDENT_COURSE JOIN STUDENT ON COURSE_ID = COURSE.ID AND STUDENT_ID = STUDENT.ID;
```

#####  Generate SQL from Metadata

-  [Standardized schema generation and data loading with JPA 2.1](https://thorben-janssen.com/standardized-schema-generation-data-loading-jpa-2-1/)
-  [Generate Database Schema with Spring Data JPA](https://www.baeldung.com/spring-data-jpa-generate-db-schema)

#####  Delete ManyToMany Association

[Hibernate Tip: Best way to remove entities from a many-to-many association](https://www.youtube.com/watch?v=vYNdjtf7iAQ&feature=emb_logo)

1.  Use a Set instead of a List
2.  Don't Use CascadeType.REMOVE
3.  Provide utility method for bidirectional associations
4.  Cleanup association when removing a referencing entity

#####  99. Step 45 - JPA Inheritance Hierarchies and Mappings - Single Table  

Disadvantages:
-  TOO many nullable columns
-  this can lead to possible errors
-  additional column DTYPE (or choose name by `DiscriminatorColumn`) - data type (`FullTimeEmployee` or `PartTimeEmployee`)

Advantages:
-  it is a single table
-  so queries are simpler

#####  100. Step 46 - JPA Inheritance Hierarchies and Mappings - Table Per Class

Advantages:
-  tables are separate
-  may have many different non-null fields

Disadvantages:
-  many columns are **repeated** (id, name, ...)
-  more complex queries
-  if we want to query all the Employees (parent) got **COMPLEX** query:

Hibernate: 
```sql
select
    employee0_.id as id1_1_,
    employee0_.name as name2_1_,
    employee0_.salary as salary1_2_,
    employee0_.hourly_wage as hourly_w1_3_,
    employee0_.clazz_ as clazz_ 
from
    ( select
        id,
        name,
        salary,
        null as hourly_wage,
        1 as clazz_ 
    from
        full_time_employee 
    union
    all select
        id,
        name,
        null as salary,
        hourly_wage,
        2 as clazz_ 
    from
        part_time_employee 
) employee0_
```

-  if we want to query all the PartTimeEmployees (child) got **SIMPLE** query:

```sql
select
    parttimeem0_.id as id1_1_,
    parttimeem0_.name as name2_1_,
    parttimeem0_.hourly_wage as hourly_w1_3_ 
from
       part_time_employee parttimeem0_
```