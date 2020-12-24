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