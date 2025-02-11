package sba.sms.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.*;
import sba.sms.models.Course;
import sba.sms.models.Student;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentServiceTest {

    private Transaction tx;
    private SessionFactory sessionFactory;
    private Session session;
    private CourseService courseService;
    private StudentService studentService;
    private String email;
    private String name;
    private String password;
    private Student expectedStudent;
    private Course expectedCourse;

    @BeforeAll
    public void setUp(){
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
        courseService = new CourseService();
        studentService = new StudentService();
        email = "Noor@gmail.com";
        name  = "Noor";
        password = "password";
    }

    @Test
    @Order(1)
    public void testCreateStudent(){

        // 1. Setup
        // expected student
        expectedStudent = new Student();
        expectedStudent.setEmail(email);
        expectedStudent.setName(name);
        expectedStudent.setPassword(password);

        // 2. Call the method to test
        studentService.createStudent(expectedStudent);

        // Actual student
        Student actualStudent = studentService.getStudentByEmail(expectedStudent.getEmail());

        // 3. Check that the executed method produced the expected result
        assertNotNull(actualStudent, "Student should exist in the database.");
        assertEquals(actualStudent.getEmail(), email);
        assertEquals(actualStudent.getName(), name);
        assertEquals(actualStudent.getPassword(), password);
    }

    @Test
    @Order(2)
    public void testCreateStudentNullEmailException() {

        // 1. Setup
        // expected student
        Student expectedStudent = new Student();
        expectedStudent.setEmail(null);
        expectedStudent.setName(name);
        expectedStudent.setPassword(password);

        // 2. Call the method to test
        studentService.createStudent(expectedStudent);

        // Actual student
        Student actualStudent = studentService.getStudentByEmail(expectedStudent.getEmail());

        // 3. Check that the executed method produced the expected result
        assertNull(actualStudent, "Student should exist in the database.");
    }

    @Test
    @Order(3)
    public void testCreateStudentNullNameException() {

        // 1. Setup
        // expected student
        Student expectedStudent = new Student();
        expectedStudent.setEmail("alex@gmail.com");
        expectedStudent.setName(null);
        expectedStudent.setPassword(password);

        // 2. Call the method to test
        studentService.createStudent(expectedStudent);

        // Actual student
        Student actualStudent = studentService.getStudentByEmail(expectedStudent.getEmail());

        // 3. Check that the executed method produced the expected result
        assertNull(actualStudent, "Student should exist in the database.");
    }

    @Test
    @Order(4)
    public void testGetStudentByEmail() {

        // 1. Setup

        // 2. Call the method to test
        Student expStudent = studentService.getStudentByEmail(expectedStudent.getEmail());

        // 3. Check that the executed method produced the expected result
        assertNotNull(expStudent, "Student should exist in the database.");
        assertEquals(expStudent.getEmail(), expectedStudent.getEmail());
        assertEquals(expStudent.getName(), expectedStudent.getName());
        assertEquals(expStudent.getPassword(), expectedStudent.getPassword());
    }

    @Test
    @Order(5)
    public void testGetStudentByEmailNullEmailOrStudentNotFound() {

        // 1. Setup

        // 2. Call the method to test
        Student expStudent = studentService.getStudentByEmail(null);

        // 3. Check that the executed method produced the expected result
        assertNull(expStudent, "IllegalArgumentException exception was thrown, Email is null.");
    }

    @Test
    @Order(6)
    public void testValidateStudent() {

        // 1. Setup

        // 2. Call the method to test
        boolean expectedIsValid= studentService.validateStudent(expectedStudent.getEmail(), expectedStudent.getPassword());

        // 3. Check that the executed method produced the expected result
        assertTrue(expectedIsValid, "Logged in student is not valid.");
    }

    @Test
    @Order(7)
    public void testValidateStudentMismatchedPassword() {

        // 1. Setup

        // 2. Call the method to test
        boolean expectedIsValid= studentService.validateStudent(expectedStudent.getEmail(), "pass");

        // 3. Check that the executed method produced the expected result
        assertFalse(expectedIsValid, "Password mismatch!!!!");
    }

    @Test
    @Order(8)
    public void testValidateStudentEmailNotInDatabase() {

        // 1. Setup

        // 2. Call the method to test
        boolean expectedIsValid= studentService.validateStudent("noor@yahoo.com", expectedStudent.getPassword());

        // 3. Check that the executed method produced the expected result
        assertFalse(expectedIsValid, "Password mismatch!!!!");
    }

    @Test
    @Order(9)
    public void registerStudentToCourseValidEmailValidCourseId(){
        // 1. setUp
        expectedCourse = new Course();
        expectedCourse.setName("Object oriented programming");
        expectedCourse.setInstructor("Alvin Smith");
        courseService.createCourse(expectedCourse);

        Course expectedCourseRetrived = courseService.getCourseById(expectedCourse.getId());

        // 2. Call the method to test
        studentService.registerStudentToCourse(expectedStudent.getEmail(), expectedCourseRetrived.getId());
        boolean isCourseinSetofCourses = studentService.getStudentCourses(expectedStudent.getEmail()).contains(expectedCourseRetrived);
       // boolean isStudentinSetofStudents = courseService.getAllCourses(expectedCourseRetrived.getId()).contains(expectedCourse);

        for (Course c : expectedStudent.getCourses()){
            System.out.println("course name" + c.getName());
        }

        // 3. Check that the executed method produced the expected result
        assertTrue(isCourseinSetofCourses, "course not in student schedule");
       // assertTrue(isStudentinSetofStudents, "student not registed in course");
    }

    // getStudentCourses
    @Test
    @Order(10)
    public void testGetStudentCourses(){
        // 1. Setup

        // 2. Call the method to test
        List<Course> students = studentService.getStudentCourses(expectedStudent.getEmail());

        // 3. Check that the executed method produced the expected result
        assertTrue(students.size() > 0, "There should be at least one course");
        assertTrue(students.contains(expectedCourse), "There should be a course in the list");
    }

    @Test
    @Order(11)
    public void testGetStudentCoursesInvalidEmail(){
        // 1. Setup

        // 2. Call the method to test
        List<Course> students = studentService.getStudentCourses("noor@yahoo.com");

        // 3. Check that the executed method produced the expected result
        assertTrue(students.size() == 0, "There should be zero courses");
    }

    @Test
    @Order(12)
    public void testGetStudentCoursesNullEmail(){
        // 1. Setup

        // 2. Call the method to test
        List<Course> courses = studentService.getStudentCourses(null);

        // 3. Check that the executed method produced the expected result
        assertTrue(courses.size() == 0, "There should be zero courses");
    }

    @Test
    @Order(13)
    public void testGetAllStudents(){
        // 1. Setup

        // 2. Call the method to test
        List<Student> students = studentService.getAllStudents();

        // 3. Check that the executed method produced the expected result
        assertTrue(students.size() > 0, "There should be at least one student");
        assertTrue(students.contains(expectedStudent), "There should be a student in the list");
    }

    @AfterAll
    public void tearDown(){
        tx.commit();
        session.close();
        sessionFactory.close();
    }
}