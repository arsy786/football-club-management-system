# Football-Club-Management-System
FCMS is a Spring Boot REST API for dealing with the management of Football Clubs.

## Motivation
The purpose of this project is to grow my knowledge of Spring Boot starting from a beginner's level. I plan to gradually implement new features to steadily grow my understanding of the framework, as well as concepts surrounding REST API development.

(Insert image of Spring Boot Roadmap)

I came across a 'Spring Boot Roadmap' and used it as a guide to structuring my learning. After boring myself with watching many hours of courses/tutorials and reading plenty of articles/forums I realised that I actually did not know how to apply ANY of new things I had 'learnt'. 

Eventually, I decided the best way to grasp these concepts was to actually implement the theory & guides into a project that interests me, designed by me!

Bored with the standard student-course REST APIs that lacked any detail or clear progression in complexity, designing my own application meant I could test my understanding and skills from the very ground up. I am a big football fan therefore the idea of building the FCMS REST API came natural to me.

I will be using this README.md file to add any notes, features, plans and details regarding the management of the entire project! This will essentially act as a blueprint for any future Spring Boot REST APIs I plan to build! :)

## Entity Relationship Diagram (ERD)
(Insert image of ERD)

I wanted to design the ERD so that I could make good practice of all relationships available.

| Description | Relationship|
| ----------- | ----------- |
| A Team has Many Players | @OneToMany |
| A Team has 1 Owner | @OneToOne |
| Many Teams play in 1 League| @ManyToOne |
| A Team has 1 Stadium | @OneToOne |
| A Team can play in Many Cups (and Many Teams can play in the same Cup) | @ManyToMany |



- A Team has Many Players
- A Team has 1 Owner
- A League has Many Teams
- A Team has 1 Stadium
- A Team can play in Many Cups (and Many Teams can play in the same Cup)

Deciding who holds the Foreign Keys in a relationship

## Spring Boot REST API

### Initialise Spring Boot Project

### Connect Spring Boot to Database

### Model/Entity Layer

### Repository Layer

### DTO

### Mapper

### Exception Handling

### Service Layer

### Controller Layer



## Extra Features to Consider Adding

### Documentation

### Logging

### Testing

### Security

### Validation




