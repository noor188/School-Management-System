package sba.sms.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Course is a POJO, configured as a persistent class that represents (or maps to) a table
 * name 'course' in the database. A Course object contains fields that represent course
 * information and a mapping of 'courses' that indicate an inverse or referencing side
 * of the relationship. Implement Lombok annotations to eliminate boilerplate code.
 */
@Entity
@Table(name = "course")
//@NoArgsConstructor
//@AllArgsConstructor
//@RequiredArgsConstructor
//@Getter
//@Setter
@ToString(exclude = "students")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CourseID")
    private int id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "instructor", length = 50, nullable = false)
    private String instructor;

    @ManyToMany(mappedBy = "courses", cascade = { CascadeType.DETACH, CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private Set<Student> students = new HashSet<>();

    public Course() {}

    public Course( String name, String instructor) {
        this.name = name;
        this.instructor = instructor;
    }

    public Course(String name, String instructor, Set<Student> students){
        this.name = name;
        this.instructor = instructor;
        this.students = students;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Course course = (Course) o;
        return this.id == course.id;
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }
}
