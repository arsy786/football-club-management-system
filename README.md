# Football-Club-Management-System
FCMS is a Spring Boot REST API for dealing with the management of Football Clubs.

## Table of Contents  
[1. Motivation](#1-motivation)
<br>
[2. REST API Design](#2-rest-api-design)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[2.1 ERD](#21-erd)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[2.2 Deciding Foreign Keys in a Relationship](#22-deciding-foreign-keys-in-a-relationship)
<br>
[3. Spring Boot REST API Development](#spring-boot-rest-api)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[3.1 Project Setup](#31-project-setup)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[3.1.1 Initialise Spring Boot Project](#311-initialise-spring-boot-project)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[3.1.2 Connect Spring Boot to Database](#312-connect-spring-boot-to-database)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[3.2 Model / Entity Layer](#32-model--entity-layer)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[3.2.1 DTO](#321-dto)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[3.2.2 Mapper](#322-mapper)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[3.3 Repository Layer](#33-repository-layer)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[3.4 Service Layer](#34-service-layer)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[3.4.1 Exception Handling](#341-exception-handling)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[3.5 Controller Layer](#35-controller-layer)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[3.5.1 Extra CRUD Methods/Endpoints](#351-extra-crud-methodsendpoints)
<br>
[4. Extra Features to Consider](#4-extra-features-to-consider)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[4.1 Documentation](#41-documentation)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[4.2 Logging](#42-logging)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[4.3 Testing](#43-testing)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[4.4 Security](#44-security)
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[4.5 Lombok - JPA](#45-lombok---jpa)
<br>


## 1. Motivation
The purpose of this project is to grow my knowledge of Spring Boot. I plan to gradually implement new features to steadily grow my understanding of the framework, as well as concepts surrounding REST API development.

![Spring Boot Roadmap](Spring-Boot-Roadmap.png)

I came across a 'Spring Boot Roadmap' and used it as a guide to structure my learning. After boring myself with watching many hours of courses/tutorials  and reading plenty of articles/forums on basic 1 entity REST APIs that lacked any progression in complexity, I realised that I actually did not know how to apply ANY of the new concepts I had 'learnt'. 

Eventually, I decided that the best way to grasp these concepts was to actually implement the theory & guides into a project that interests me (and is also designed by me)! 

Designing my own application means that I can test my understanding and skills from the very ground up. I am a fan of football therefore the idea of building the FCMS REST API came natural to me.

I will be using this README.md file to add any notes, features, plans and details regarding the management of the entire project! This will essentially serve as a blueprint for any future Spring Boot REST APIs I plan to build! :)

## 2. REST API Design

### 2.1 ERD
![FCSM ERD](FCMS-ERD.png)

I wanted to design the Entity Relationship Diagram (ERD) so that I could make use of all JPA relationships available.

| Description | JPA Relationship|
| ----------- | ----------- |
| A Team has Many Players | @OneToMany |
| Many Teams play in 1 League| @ManyToOne |
| A Team has 1 Owner | @OneToOne |
| A Team has 1 Stadium | @OneToOne |
| A Team can play in Many Cups (and Many Teams can play in the same Cup) | @ManyToMany |

### 2.2 Deciding Foreign Keys in a Relationship
@ManyToOne: 
- FK (and config) in Many (Child) side. 
- If you want a bidirectional relationship, in Parent entity, must add List< Child > field  and map the corresponding @OneToMany annotation.

Bi-directional Relationship Example:
```java
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long childId;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "parentId") //fk
    private Parent parent;
}

public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parentId;
    
    @OneToMany(mappedBy = "parent")
    private List<Child> children;
}
```

@ManyToMany: 
- Need intermediate table (Join Table) with FK from both sides, which combine to form a composite key. 
- This can be configured on either side in Java, but it is preferred to do the config in the Dependent entity.
- For example, X can have many Y's AND Y can have many X's.

Team (Independent) and Cup (Dependent) relationship Example:
```java
public class Cup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cupId;

    @ManyToMany
    @JoinTable(
            name = "team_cup_map",
            joinColumns = @JoinColumn(name = "cup_id", referencedColumnName = "cupId"),
            inverseJoinColumns = @JoinColumn(name = "team_id", referencedColumnName = "teamId")
    ) // both fk's for intermediate table
    private List<Team> teams;
}

public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @ManyToMany(mappedBy = "teams")
    private List<Cup> cups;
}
```

@OneToOne / OneTo(Perhaps)One: 
- FK in any side. 
- FK (config) preferred in Dependent entity (if one exists).
- For example, a Student is independent of a Student Mentor,
  the Student Mentor only exists if the Student does. Therefore, Student Mentor dependent on Student.

Student (Indepdendent) and Student Mentor (Dependent relationship Example:
```java
public class StudentMentor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentMentorId;

    @OneToOne
    @JoinColumn(name = "student_id", referencedColumnName = "studentId") //fk
    private Student student;
}

public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    @OneToOne(mappedBy = "student")
    private StudentMentor studentMentor;
}
```

NOTE: FK in Child/Dependent table references its PK in Parent/Independent table.

Q) How to decide if you should have 1:1 table or merge the attributes into 1 table?
<br>
A) If data in one table is related to, BUT does NOT 'belong' to the entity described by the other, then keep separate.

## 3. Spring Boot REST API

*DISCLAIMER: All features (Entity, DTO, Controller, etc.) associated with 'Team' will aim to maintain the highest coding standards; implement the most concepts; and contain the most detail in regard to complying with REST API development standards.*

*Features associated with other Models (Player, Owner, etc.) will only contain what is necessary and convenient for a project of this scale e.g. other Controllers will not contain OpenAPI documentation simply for convenience, code readability and reusability.*

### 3.1 Project Setup

### 3.1.1 Initialise Spring Boot Project

- [Spring Initalizr](https://start.spring.io) 
- Maven, Java, Jar, 8
- MUST: Spring Web, Spring Data JPA, Database (H2, Mongo, etc.)
- OPTIONAL: Spring Rest Dev Tools, REST Repos, Spring Web Services etc.
- [Maven artifact and group naming conventions](https://stackoverflow.com/questions/23172586/maven-artifact-and-group-naming-conventions)

### 3.1.2 Connect Spring Boot to Database

- Connect easily via Spring Data JPA.
- Add information to applications.properties file to setup DB connections (and other features i.e. port).
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

- file-name.sql will contain SQL statements to populate the entities created by JPA/Hibernate during Spring Boot start-up:
```sql
INSERT INTO team (name, city, manager)
VALUES ('Manchester United F.C.', 'Manchester', 'Erik ten Hag');

INSERT INTO team (name, city, manager)
VALUES ('Manchester City F.C.', 'Manchester', 'Pep Guardiola');
```

### 3.2 Model / Entity Layer

- Create a POJO for your Models/Entities.
- This class (entity) will be registered with Hibernate with the correct JPA Annotations.
- Can add attributes (name, nullable, unique, etc.) to JPA Annotations (@Table, @Column, etc.) to add constraints to DB.
- Can use Lombok annotations to reduce boilerplate code.

NOTE: Be careful of Lombok - JPA integration.

JPA Annotations with Lombok Example:

```java
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String userName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "bio")
    private String bio;

}
```
- [Link to understanding JPA Entities and Annotations](https://www.baeldung.com/jpa-entities)

### 3.2.1 DTO

- DTO (data transfer object) is an object that carries data between processes.
- DTOs for JPA entities generally contain a subset of entity attributes.
- For example, if you need to expose only a few of the entity attributes via REST API, you can map entities to DTOs with those attributes and serialize only them.
- Basically, DTOs allow you to decouple presentation/business logic layer from the data access layer.
- Can use Lombok annotations to reduce boilerplate code.

NOTE: [Can use JPA Buddy to generate DTOs](https://www.youtube.com/watch?v=_u-qn-R4DoA)
<br>
NOTE: DTOs solve Jackson JSON infinite recursion problem for bidirectional relationships.

User Model vs. User DTO Example: 

```java
@Data
public class User {
    private String id;
    private String name;
    private String password;
    private List<Role> roles;
}

@Data
public class UserDto {
    private String name;
    private List<String> roles;
}
```

- Can use DTOs to reference other entities via associations. 

OwnerDto containing PetDto id and name Example:
```java
@Data
public class OwnerDto implements Serializable {
    private final Long ownerId;
    private final String name;
    private final String country;
    private final List<PetDto> pets;

    @Data
    public static class PetDto implements Serializable {
        private final Long petId;
        private final String name;
    }
}
```

- Can use Java Bean Validation annotations on fields (@NonBlank, @Email, etc.) to validate user inputs.
- Some annotations accept additional attributes, but the message attribute is common to all of them. This is the message that will usually be rendered when the value of the respective property fails validation.

Java Bean Validation Example:
```java
public class UserDto {

  @NotNull(message = "Name cannot be null")
  private String name;

  @NotEmpty
  @Size(min = 8, message = "password should have at least 8 characters")
  private String password;

  @Size(min = 10, max = 200, message
          = "About Me must be between 10 and 200 characters")
  private String aboutMe;

  @Min(value = 18, message = "Age should not be less than 18")
  @Max(value = 150, message = "Age should not be greater than 150")
  private int age;

  @Email(message = "Email should be valid")
  private String email;

  // standard setters and getters 
}
```

- [Link to Java Bean Validation Basics](https://www.baeldung.com/javax-validation#overview)


### 3.2.2 Mapper

- Mapper is a technique to transfer data from DTOs to Entitys or vice versa.
- MapStruct is a code generator that greatly simplifies the implementation of mappings.
- When using MapStruct, you only define simple method signatures, converting Entity to DTO, DTO to Entity, List of Entity to List of DTOs.
- MapStruct annotation (@Mapper) will generate implementation code for you during build time.

NOTE: [Can use JPA Buddy to generate Mappers](https://www.youtube.com/watch?v=_u-qn-R4DoA)

Example of Mapper class with MapStruct @Mapper Annotation:
```java
@Mapper(componentModel = "spring")
public interface UserMapper {
    
    UserDto userToUserDto (User user);
    
    List<UserDto> usersToUsersDto(List<User> users);
    
    User userDtoToUser(UserDto userDto);
}
```

### 3.3 Repository Layer

- This will interact with the underlying DB.
- To program CRUD (and JPA) operations on Student entities, need to have a StudentRepository interface.
- Spring Data provides a layer on top of JPA that offers convenient ways to reduce boiler plate code.
- CrudRepository interface extends Repository to provide CRUD functionality. 
- JpaRepository interface extends CrudRepository to give us the JPA specific features.
- For functions that are not already present in JpaRepo, add new methods/queries in the StudentRepository interface.
- For simple queries, Spring can easily derive what the query should be from just the method name.
- Example of simple query: 

```java
public interface BookRepository extends JpaRepository<Book, Long> {
  
    List<Book> findByName(String name);

}
```

- For more complex queries, can annotate a repository method with the @Query annotation where the 	value contains the JPQL or SQL to execute.
- Example of complex queries:
```java
public interface BookRepository extends JpaRepository<Book, Long> {
  
    @Query("select b from Book b where upper(b.title) like concat('%',upper(:title), '%')")
    List<Book> findByTitle(@Param("title") String title);

}

public interface StudentRepository extends JpaRepository<Student, Long> {
    
    @Query("SELECT s FROM Student s WHERE s.email = ?1")
    Optional<Student> findStudentByEmail(String email);

}
```

### 3.4 Service Layer

- The Service is where all the implementation is done and interacts with the repository (DB) and controller.
- It will only take the data from the controller layer and transfer it to the repository layer. It will also take the data from the repository layer and send it back to Controller Layer.
- The Service exposes methods that will be called from the Controller.
- Here is where all the business logic code is implemented.
- These are basic CRUD methods.
- DTOs are injected in this layer, as any response being passed to the Controller must be in the form of a DTO.

*For Implementation, see source code in this repo*

### 3.4.1 Exception Handling

- Handling errors correctly in APIs while providing meaningful error messages is a very desirable feature, as it can help the API client properly respond to issues. The default behavior tends to be returning stack traces that are hard to understand and ultimately useless for the API client.
- Partitioning the error information into fields enables the API client to parse it and provide better error messages to the user.
- [Spring Boot Tutorial | How To Handle Exceptions](https://www.youtube.com/watch?v=PzK4ZXa2Tbc&t=355s)
- [Guide to Spring Boot REST API Error Handling](https://www.toptal.com/java/spring-boot-rest-api-error-handling)

*For Implementation, see source code in this repo*

### 3.5 Controller Layer

- Now we can implement a controller class to define our API URLs and use the service class to manage data.
- Different methods are defined in the controller, these methods will be called by different endpoints.
- The endpoint methods in the Controller typically match those in its corresponding Service Layer.
- Controller consumes (via endpoint) and responds (via service) with DTO's only.
- Handles HTTP requests
- Can use Java Bean Validation annotation (@Valid) to enable validation in Controller layer. 
- The @Valid annotation ensures the validation of the whole object in a method argument.

The Basic/Standard HTTP REST API calls:

| Basic HTTP Calls 
| ----------- | 
|(GET) getAllEntities|
|(GET) getEntityById|
|(POST) createEntity|
|(PUT) updateEntityById|
|(DELETE) deleteEntityById|

*For Implementation, see source code in this repo*

### 3.5.1 Extra CRUD Methods/Endpoints

- Once the basic CRUD methods are added to the Service/Controller layers, if the entity has relationships with other entities... Extra CRUD methods need to be implemented.
- ManyToOne: logic in child service layer
- OneToOne: logic is either not required OR in the dependent entity service layer (e.g. Student ID exists if Student does)
- ManyToMany: logic in either side OR in the dependent side (if applicable)

The Extra HTTP REST API calls for entity rships:

| Entity Relationship | Extra CRUD Methods/Endpoints|
| ----------- | ----------- |
| @ManyToOne |  (GET) viewAllChildrenForParent <br> (PUT) addChildToParent <br> (DELETE) removeChildFromParent |
| @OneToOne | (GET) viewDependentForIndependent <br> (PUT) addDependentToIndependent <br> (DELETE) removeDependentFromIndependent |
| @ManyToMany |  (GET) viewAllDependentsForIndependent <br> (PUT) addDependentToIndependent <br> (DELETE) removeDependentFromIndependent |

*For Implementation, see source code in this repo*


Note: Service Layer logic differs between @ManyToMany and @ManyToOne relationships

For Example:

@ManyToOne: To check if Child contains Parent (checking an Object)
```java
if (Objects.nonNull(player.getTeam()))
```
@ManyToMany: To check if Dependent contains Independent (checking a Collection of Objects)

```java
if (cup.getTeams.contains(team))
```
## 4. Extra Features to Consider

### 4.1 Documentation

- Swagger/OpenAPI
- Add annotations in Controller layer

### 4.2 Logging

- Slf4j in Exception Handler and Controller layer

### 4.3 Testing

- Unit Test: Controller(s) & Service(s) via JUnit & Mockito
- Integration Testing: Endpoints (Controller) via Cucumber/Gherkin or MockMvc/RestTemplate/TestRestTemplate

### 4.4 Security

- JWT

### 4.5 Lombok - JPA

When working with JPA and Lombok, remember these rules:

- Avoid using @EqualsAndHashCode and @Data with JPA entities
- Always exclude lazy attributes when using @ToString
- Don’t forget to add @NoArgsConstructor to entities with @Builder or @AllArgsConstructor.

Source: [Lombok and JPA: What Could Go Wrong?](https://dzone.com/articles/lombok-and-jpa-what-may-go-wrong)

