package sba.sms.services;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import sba.sms.dao.CourseI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * CourseService is a concrete class. This class implements the
 * CourseI interface, overrides all abstract service methods and
 * provides implementation for each method.
 */
public class CourseService implements CourseI {

    private SessionFactory sessionFactory;

    public CourseService() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    // MODIFY: This
    // EFFECT: persist course to database, also handle
    //         commit,rollback, and exceptions
    @Override
    public void createCourse(Course course){
        try(Session session = sessionFactory.openSession()){
            Transaction tx = session.beginTransaction();

            try
            {
                session.persist(course);
                tx.commit();
                System.out.println("course object created successfully!");
            }catch(ConstraintViolationException e) {
                System.out.println("Some constrains on the course fields were violated");
                tx.rollback();
            }catch(Exception e){
                System.out.println("course could not be created!");
                tx.rollback();
            }
        }
    }

    // EFFECT: return course if exists, also handle commit,
    //         rollback, and exceptions
    @Override
    public Course getCourseById(int courseId){
        try(Session session = sessionFactory.openSession()){
            return session.get(Course.class, courseId);
        }
    }

    // EFFECT: return all courses from database,
    //         also handle commit,rollback, and exceptions
    @Override
    public List<Course> getAllCourses(){
        try(Session session = sessionFactory.openSession()){
            String hql = "SELECT c FROM Course c";
            return session.createQuery(hql, Course.class).list();
        }
    }

}
