package com.csye6225.fall2018.courseservice.datamodels;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Professor
{
    private String firstName;
    private String department;
    private long professorId;
    private Date joiningDate;
    private Set<String> coursesTaught;

    public Professor()
    {
        this.coursesTaught = new HashSet<>();
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getDepartment()
    {
        return department;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }

    public long getProfessorId()
    {
        return professorId;
    }

    public void setProfessorId(long professorId)
    {
        this.professorId = professorId;
    }

    public Date getJoiningDate()
    {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate)
    {
        this.joiningDate = joiningDate;
    }

    public Set<String> getCoursesTaught()
    {
        return coursesTaught;
    }

    public void setCoursesTaught(Set<String> coursesTaught)
    {
        this.coursesTaught = coursesTaught;
    }

}
