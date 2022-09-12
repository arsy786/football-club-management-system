# Football-Club-Management-System
FCMS is a Spring Boot REST API for dealing with the management of Football Clubs.

## Table of Contents  
[Motivation](#motivation)

[ERD](#erd)

[Spring Boot Rest API](#spring-boot-rest-api) 

[Extra Features to Consider](#extra-features-to-consider) 

## Motivation
The purpose of this project is to grow my knowledge of Spring Boot starting from a foundation level to an advanced level. I plan to gradually implement new features to steadily grow my understanding of the framework, as well as concepts surrounding REST API development.

![Spring Boot Roadmap](Spring-Boot-Roadmap.png)

I came across a 'Spring Boot Roadmap' and used it as a guide to structuring my learning. After boring myself with watching many hours of courses/tutorials and reading plenty of articles/forums I realised that I actually did not know how to apply ANY of new things I had 'learnt'. 

Eventually, I decided that the best way to grasp these concepts is to actually implement the theory & guides into a project that interests me, and is also designed by me!

Bored with the standard student-course REST APIs that lacked any detail or clear progression in complexity, designing my own application means that I can test my understanding and skills from the very ground up. I am a big football fan therefore the idea of building the FCMS REST API came natural to me.

I will be using this README.md file to add any notes, features, plans and details regarding the management of the entire project! This will essentially serve as a blueprint for any future Spring Boot REST APIs I plan to build! :)

## Entity Relationship Diagram (ERD)

![FCSM ERD](FCMS-ERD.png)

I wanted to design the ERD so that I could make good practice of all JPA relationships available.

| Description | Relationship|
| ----------- | ----------- |
| A Team has Many Players | @OneToMany |
| A Team has 1 Owner | @OneToOne |
| Many Teams play in 1 League| @ManyToOne |
| A Team has 1 Stadium | @OneToOne |
| A Team can play in Many Cups (and Many Teams can play in the same Cup) | @ManyToMany |



### Deciding who holds the Foreign Keys in a relationship
- ManyToOne: FK in Many(Child) side. If you want a bidirectional relationship, must add List<> or Set<> field in Parent entity and map the corresponding @OneToMany annotation.
- ManyToMany: need intermediate table (Join Table) with FK from both sides and can be configured on either side in Java.
- OneToOne: FK in any side.
- OneTo(Perhaps)One: FK in dependent entity. E.g. A Country is independent of a UN Representative, the UN Rep only exists if the Country does. Therefore UN Rep dependent on Country so PK country_id is FK in UN Rep table.

NOTE: FK in Child table references its PK in Parent table.

NOTE: ManyToMany Example: X can have many Y's AND Y can have many X's.


Q) How to decide if you should have 1:1 table or merge the attributes into 1 table?

A) If data in one table is related to, BUT does NOT 'belong' to the entity described by the other, then keep separate.

## Spring Boot REST API

### Initialise Spring Boot Project

- [Spring Initalizr](https://start.spring.io) 
- Maven, Java, Jar, 8
- MUST: Spring Web, Spring Data JPA, Database (H2, Mongo, etc.)
- OPTIONAL: Spring Rest Dev Tools, REST Repos, Spring Web Services etc.
- [Maven artifact and group naming conventions](https://stackoverflow.com/questions/23172586/maven-artifact-and-group-naming-conventions)

### Connect Spring Boot to Database

- Connect easily via Spring Data JPA
- Add information to applications.properties file to setup DB connections (and other features i.e. port)
- Example of H2 database connection setup with console enabled and data pre-loaded: 
```properties
server.port=8080

spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.platform=h2

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.defer-datasource-initialization=true
spring.jpa.hibernate.ddl-auto=create-drop

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

- Can add initial data by adding file-name.sql in resources folder and adding these lines to application.properties:
```properties
spring.sql.init.mode=always
spring.sql.init.data-locations=classpath:file-name.sql
```

### Model/Entity Layer

- Create a POJO for your Models/Entities
- This class (entity) will be registered with Hibernate with the correct JPA Annotations
- Can use Lombok annotations (@Data) to reduce boiler plate code
- [Link to understanding JPA Entities and Annotations](https://www.baeldung.com/jpa-entities)

### Repository Layer

- To program CRUD (and JPA) operations on Student entities, need to have a StudentRepository interface
- Spring Data provides a layer on top of JPA that offers convenient ways to reduce boiler plate code.
- CrudRepository interface extends Repository to provide CRUD functionality
- JpaRepository interface extends CrudRepository to give us the JPA specific features
- For functions that are not already present in JpaRepo, add new methods/queries in the StudentRepository interface
- No method body is required as Spring will provide the boilerplate code
- For simple queries, Spring can easily derive what the query should be from just the method name.
- Example of simple query: 
```java
List<Book> findByName(String name);
```

- For more complex queries, can annotate a repository method with the @Query annotation where the 	value contains the JPQL or SQL to execute.
- Example of complex query:
```java
@Query("select b from Book b where upper(b.title) like concat('%',upper(:title), '%')")
List<Book> findByTitle(@Param("title") String title);
```

### DTO

### Mapper

### Exception Handling

### Service Layer

### Controller Layer

### Extra CRUD Methods/Endpoints


## Extra Features to Consider

### Documentation

### Logging

### Testing

### Security

### Validation




