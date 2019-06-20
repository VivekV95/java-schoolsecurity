package com.lambdaschool.school.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course")
public class Course {
    @ApiModelProperty(name = "courseid",
            value = "Primary key for Course", required = true,
            example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @JsonView(View.CoursesOnly.class)
    private long courseid;

    //    @JsonView(View.CoursesOnly.class)
    @ApiModelProperty(name = "coursename",
            value = "Course Name", required = true,
            example = "Physics")
    private String coursename;

    @ApiModelProperty(name = "instructid",
            value = "Foreign Key for Instructor",
            example = "1")
    @ManyToOne
    @JoinColumn(name = "instructid")
    @JsonIgnoreProperties("courses")
//    @JsonView(View.CoursesOnly.class)
    private Instructor instructor;

    @ManyToMany(mappedBy = "courses")
    @JsonIgnoreProperties("courses")
    private List<Student> students = new ArrayList<>();

    public Course() {
    }

    public Course(String coursename) {
        this.coursename = coursename;
    }

    public Course(String coursename, Instructor instructor) {
        this.coursename = coursename;
        this.instructor = instructor;
    }

    public long getCourseid() {
        return courseid;
    }

    public void setCourseid(long courseid) {
        this.courseid = courseid;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
