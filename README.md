# Master Hibernate and JPA with Spring Boot in 100 Steps

## Tutorial from in28Minutes (Udemy)

#####  N plus One Problem

[N+1 query problem with JPA and Hibernate](https://vladmihalcea.com/n-plus-1-query-problem/)

Unlike the default fetch plan, which you are using when calling the find method of the EntityManager, 
a JPQL or Criteria API query defines an explicit plan that Hibernate cannot change by injecting a JOIN FETCH automatically. 
So, you need to do it manually.

-  `JOIN FETCH` rules