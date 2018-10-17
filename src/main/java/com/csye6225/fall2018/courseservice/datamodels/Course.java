package com.csye6225.fall2018.courseservice.datamodels;

import java.util.HashSet;
import java.util.Set;

public class Course
{
    private String courseId;
    private Set<Long> lectures;
    private String board;
    private Set<Long> roster;
    private long professorId;
    private long studentTAId;
    private String programName;

    public Course()
    {
        this.lectures = new HashSet<>();
        this.roster = new HashSet<>();
    }

    public String getCourseId()
    {
        return courseId;
    }

    public void setCourseId(String courseId)
    {
        this.courseId = courseId;
    }

    public String getBoard()
    {
        return board;
    }

    public void setBoard(String board)
    {
        this.board = board;
    }

    public long getProfessorId()
    {
        return professorId;
    }

    public void setProfessorId(long professorId)
    {
        this.professorId = professorId;
    }

    public long getStudentTAId()
    {
        return studentTAId;
    }

    public void setStudentTAId(long studentTAId)
    {
        this.studentTAId = studentTAId;
    }

    public Set<Long> getLectures()
    {
        return lectures;
    }

    public void setLectures(Set<Long> lectures)
    {
        this.lectures = lectures;
    }

    public String getProgramName()
    {
        return programName;
    }

    public void setProgramName(String programName)
    {
        this.programName = programName;
    }

    public void setRoster(Set<Long> roster)
    {
        this.roster = roster;
    }

    public Set<Long> getRoster()
    {
        return roster;
    }

}
