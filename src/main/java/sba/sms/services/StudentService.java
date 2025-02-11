package sba.sms.services;

import jakarta.persistence.PersistenceException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * StudentService is a concrete class. This class implements the
 * StudentI interface, overrides all abstract service methods and
 * provides implementation for each method. Lombok @Log used to
 * generate a logger file.
 */

public class StudentService implements StudentI{

    private SessionFactory sessionFactory;

    public StudentService() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    // MODIFIE: This
    // EFFECT:  persist student to database, also handle commit,
    //          rollback, and exceptions
    @Override
    public void createStudent(Student student){
        try(Session session = sessionFactory.openSession()){
            Transaction tx = session.beginTransaction();

            try
            {
                session.persist(student);
                tx.commit();
                System.out.println("Studnet object created successfully!");
            }catch(ConstraintViolationException e) {
                System.out.println("Some constrains on the student fields were violated");
                tx.rollback();
            }catch(PersistenceException e){
                System.out.println("Email (ID) shouldn't be null!!");
                tx.rollback();
            }

        }
    }

    // EFFECT: return student if exists, also handle commit,
    //         rollback, and exceptions
    @Override
    public Student getStudentByEmail(String email){
        try(Session session = sessionFactory.openSession()){
            Student student;
            try {
                student = session.get(Student.class, email);
            }catch(IllegalArgumentException e){
                System.out.println("Student could not be found!");
                return null;
            }

            return student;
        }
    }

    // EFFECT: match email and password to database to gain access to
    //         courses, also handle commit,rollback, and exceptions
    @Override
    public boolean validateStudent(String email, String password){
        try(Session session = sessionFactory.openSession()){
            Transaction tx = session.beginTransaction();
            Student student;
            boolean valid;

            try {
                student = session.get(Student.class, email);
            }catch(IllegalArgumentException e){
                System.out.println("Student could not be found!");
                tx.rollback();
                return false;
            }
            if (student != null){
                valid = password.equals(student.getPassword());
                System.out.println("matching passwords .....");
            }else{
                valid = false;
            }
            tx.commit();
            // “Wrong Credentials”
            return valid;
        }
    }

    // MODIFIE: This
    // EFFECT:  register a course to a student (collection to prevent duplication),
    //          also handle commit,rollback, and exceptions
    public void registerStudentToCourse(String email, int courseId){
        try(Session session = sessionFactory.openSession()){
            Transaction tx = session.beginTransaction();
            Student student;
            Course course;

            try {
                student = session.get(Student.class, email);
                course = session.get(Course.class, courseId);

                if (student == null && course == null){
                    System.out.println(student == null ? "Student not found" : "Course not found" );
                    tx.rollback();
                }else if (student.getCourses().contains(course) || course.getStudents().contains(student)) {
                    System.out.println("Student already registered!");
                    tx.rollback();
                } else {
                    student.getCourses().add(course);
                    course.getStudents().add(student);
                    System.out.println("Added student to course succesfully!!!");
                    tx.commit();
                }
            }catch(IllegalArgumentException e){
                System.out.println("Student could not be found!");
                tx.rollback();
            }
        }
    }

    // EFFECT: get all the student courses list (use native query),
    //         also handle commit,rollback, and exceptions
    @Override
    public List<Course> getStudentCourses(String email){
            try(Session session = sessionFactory.openSession()){
                Transaction tx = session.beginTransaction();
                Student student;

                try {
                    student = session.get(Student.class, email);
                }catch(IllegalArgumentException e){
                    System.out.println("Student could not be found!");
                    tx.rollback();
                    return new ArrayList<>();
                }

                if (student == null){
                    System.out.println("Student does not exist in the database!");
                    return new ArrayList<>();
                }

                Set<Course> courses = student.getCourses();
                List<Course> courseList = new ArrayList<>(courses);
                tx.commit();
                return courseList;
            }
    }

    // EFFECT: return all students from database, also handle commit,
    //         rollback, and exceptions
    @Override
    public List<Student> getAllStudents(){
        try(Session session = sessionFactory.openSession()){
            String hql = "SELECT s FROM Student s";
            return session.createQuery(hql, Student.class).list();
        }
    }



}
