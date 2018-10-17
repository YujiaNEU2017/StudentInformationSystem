package com.csye6225.fall2018.courseservice.datamodels;

import java.util.HashSet;
import java.util.Set;

public class Program
{
    private String programName;
    private Set<String> courses;
    private Set<Long> students;

    public Program()
    {
        this.courses = new HashSet<>();
        this.students = new HashSet<>();
    }

    public String getProgramName()
    {
        return programName;
    }

    public void setProgramName(String programName)
    {
        this.programName = programName;
    }

    public Set<String> getCourses()
    {
        return courses;
    }

    public void setCourses(Set<String> courses)
    {
        this.courses = courses;
    }

    public Set<Long> getStudents()
    {
        return students;
    }

    public void setStudents(Set<Long> students)
    {
        this.students = students;
    }
}
