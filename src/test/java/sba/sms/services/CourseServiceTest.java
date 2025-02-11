package sba.sms.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import sba.sms.models.Course;
import sba.sms.models.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CourseServiceTest {

    private Transaction tx;
    private SessionFactory sessionFactory;
    private Session session;
    private CourseService courseService;
    private StudentService studentService;
    private String name;
    private String instructor;
    private Course expectedCourse;
    private int expectedCourseId;

    @BeforeAll
    public void setUp(){
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
        courseService = new CourseService();
        studentService = new StudentService();
        name = "Object oriented programming";
        instructor  = "Alvin Smith";
    }

    @Test
    @Order(1)
    public void testCreateCourse(){

        // 1. Setup
        // expected student
        expectedCourse = new Course();
        expectedCourse.setName(name);
        expectedCourse.setInstructor(instructor);

        // 2. Call the method to test
        courseService.createCourse(expectedCourse);
        expectedCourseId = expectedCourse.getId();

        // Actual student
        Course actualCourse = courseService.getCourseById(expectedCourse.getId());

        // 3. Check that the executed method produced the expected result
        assertNotNull(actualCourse, "Student should exist in the database.");
        assertEquals(actualCourse.getName(), name);
        assertEquals(actualCourse.getInstructor(), instructor);
    }

    @Test
    @Order(2)
    public void testCreateCourseNullNameException() {

        // 1. Setup
        // expected student
        Course expectedCourse = new Course();
        expectedCourse.setName(null);
        expectedCourse.setInstructor(instructor);

        // 2. Call the method to test
        courseService.createCourse(expectedCourse);

        // Actual student
        Course actualCourse = courseService.getCourseById(expectedCourse.getId());

        // 3. Check that the executed method produced the expected result
        assertNull(actualCourse, "Course should exist in the database.");
    }

    @Test
    @Order(3)
    public void testGetCourseById() {

        // 1. Setup

        // 2. Call the method to test
        Course expCourse = courseService.getCourseById(expectedCourseId);

        // 3. Check that the executed method produced the expected result
        assertNotNull(expCourse, "Course should exist in the database.");
        assertEquals(expCourse.getName(), expectedCourse.getName());
        assertEquals(expCourse.getInstructor(), expectedCourse.getInstructor());
    }

    @Test
    @Order(5)
    public void testGetCourseByIdStudentNotFound() {

        // 1. Setup

        // 2. Call the method to test
        Course expCourse = courseService.getCourseById(5);

        // 3. Check that the executed method produced the expected result
        assertNull(expCourse, "Student not found in the database.");
    }    

    @Test
    @Order(13)
    public void testGetAllCourses(){
        // 1. Setup

        // 2. Call the method to test
        List<Course> courses = courseService.getAllCourses();

        // 3. Check that the executed method produced the expected result
        assertTrue(courses.size() > 0, "There should be at least one course");
        assertTrue(courses.contains(expectedCourse), "There should be a course in the list");
    }

    @AfterAll
    public void tearDown(){
        tx.commit();
        session.close();
        sessionFactory.close();
    }
}
