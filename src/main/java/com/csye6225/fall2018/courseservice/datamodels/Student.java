package com.csye6225.fall2018.courseservice.datamodels;

import java.util.HashSet;
import java.util.Set;

public class Student
{
    private String name;
    private long studentId;
    private String imageURL;
    private Set<String> coursesEnrolled;
    private String programName;
    private Set<String> coursesAssisted;

    public Student()
    {
        this.coursesEnrolled = new HashSet<>();
        this.coursesAssisted = new HashSet<>();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public long getStudentId()
    {
        return studentId;
    }

    public void setStudentId(long studentId)
    {
        this.studentId = studentId;
    }

    public Set<String> getCoursesEnrolled()
    {
        return coursesEnrolled;
    }

    public void setCoursesEnrolled(Set<String> coursesEnrolled)
    {
        this.coursesEnrolled = coursesEnrolled;
    }

    public String getImageURL()
    {
        return imageURL;
    }

    public void setImageURL(String imageURL)
    {
        this.imageURL = imageURL;
    }

    public String getProgramName()
    {
        return programName;
    }

    public void setProgramName(String programName)
    {
        this.programName = programName;
    }

    public Set<String> getCoursesAssisted()
    {
        return coursesAssisted;
    }

    public void setCoursesAssisted(Set<String> coursesAssisted)
    {
        this.coursesAssisted = coursesAssisted;
    }
}
