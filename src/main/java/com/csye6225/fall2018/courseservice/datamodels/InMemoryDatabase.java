package com.csye6225.fall2018.courseservice.datamodels;

import java.util.HashMap;
import java.util.Map;

public class InMemoryDatabase
{
    private static Map<Long, Professor> professorsDB = new HashMap<>();

    private static Map<Long, Student> studentsDB = new HashMap<>();

    private static Map<String, Program> programsDB = new HashMap<>();

    private static Map<String, Course> coursesDB = new HashMap<>();

    private static Map<Long, Lecture> lecturesDB = new HashMap<>();

    public static Map<Long, Professor> getProfessorsDB()
    {
        return professorsDB;
    }

    public static Map<Long, Student> getStudentsDB()
    {
        return studentsDB;
    }

    public static Map<String, Program> getProgramsDB()
    {
        return programsDB;
    }

    public static Map<String, Course> getCoursesDB()
    {
        return coursesDB;
    }

    public static Map<Long, Lecture> getLecturesDB()
    {
        return lecturesDB;
    }
}
