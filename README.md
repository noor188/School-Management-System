## School Management System

### End goal
1. Core Java
2. Hibernate
3. JUnit 

### Business Requirement:
Created a basic School Management System
where students can register for courses, and view the
course assigned to them.

### Work-Flow:
Only students with the right credentials can log in.
Otherwise, a message is displayed stating: “Wrong Credentials”.
Valid students are able to see the courses they are registered for.
Valid students are able to register for any course in the system as
long as they are not already registered.
https://github.com/noor188/School-Management-System
### Maven Project Requirements:

- [Amazon Corretto 17 JDK](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html) 
- [Hibernate](https://mvnrepository.com/artifact/org.hibernate/hibernate-core)
- [MySQL Connector Java](https://mvnrepository.com/artifact/mysql/mysql-connector-java)
- [Project lombok](https://mvnrepository.com/artifact/org.projectlombok/lombok)
- [Junit jupiter api](https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api)
- [Junit jupiter engine](https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-engine)
- [Junit jupiter param](https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-params)
- [Junit platform suite](https://mvnrepository.com/artifact/org.junit.platform/junit-platform-suite-engine)
- [Junit platform runner](https://mvnrepository.com/artifact/org.junit.platform/junit-platform-runner)

#### Requirement 1 - Models:
1. Created Model classes named Student.java and Course.java under Model package.
- Each Model class has:
     - no args constructor
     - all args constructor
     - required args constructor
     - setters and getter
     - toString (excluded collections to avoid infinite loops)
     - override equals and hashcode methods
#### Requirement 2 - Data Access Object  (dao) interfaces:
Created the following classes which implements the StudentI and CourseI interfaces, as well as overrides and implements all abstract service methods.

- StudentService
- CourseService

#### Requirement 3 - Service layer:

#### Requirement 4 - Utility classes:

#### Requirement 5 - JUnit:

#### Project Tree 
#### **Project Structure:**

```
/src/main/java/sba/sms/
├── dao/
│   ├── CourseI
│   ├── StudentI
│
├── model/
│   ├── Course.java
│   ├── Student.java
│
├── service/
│   ├── CourseService.java
│   ├── StudentService.java
|
├── utils/
│   ├── CommandLine.java
│   ├── HibernateUtil.java
|
└── App.java (starter code for managing students)

/src/test/java/sba/sma/
├── service/
│   ├── studentServiceTest.java 
└── 
```

#### Workflow example
![workflow example](images/example.png)
