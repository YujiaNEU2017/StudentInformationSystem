package com.csye6225.fall2018.courseservice.services;

import java.util.List;
import java.util.Map;

import com.csye6225.fall2018.courseservice.datamodels.Course;
import com.csye6225.fall2018.courseservice.datamodels.InMemoryDatabase;
import com.csye6225.fall2018.courseservice.datamodels.Program;
import com.csye6225.fall2018.courseservice.datamodels.Student;

import jersey.repackaged.com.google.common.collect.Lists;

public class ProgramsService
{
    final private static Map<String, Program> programMap = InMemoryDatabase.getProgramsDB();
    final private static Map<String, Course> courseMap = InMemoryDatabase.getCoursesDB();
    final private static Map<Long, Student> studentMap = InMemoryDatabase.getStudentsDB();

    public List<Program> getAllPrograms()
    {
        return Lists.newArrayList(programMap.values());
    }

    public Program addProgram(final Program program)
    {
        final String programName = program.getProgramName();
        if (programMap.containsKey(programName) || !verifyProgram(program))
        {
            return null;
        }
        programMap.put(programName, program);
        addToOtherDB(program);
        return programMap.get(programName);
    }

    public Program getProgram(final String programName)
    {
        return programMap.get(programName);
    }

    public Program updateProgram(final String programName, final Program program)
    {
        final Program oldProgram = programMap.get(programName);
        if (oldProgram == null || !verifyProgram(program))
        {
            return null;
        }
        program.setProgramName(oldProgram.getProgramName());
        programMap.put(oldProgram.getProgramName(), program);
        deleteFromOtherDB(oldProgram);
        addToOtherDB(program);
        return program;
    }

    public Program deleteProgram(final String programName)
    {
        final Program oldProgram = programMap.get(programName);
        if (oldProgram == null)
        {
            return null;
        }
        programMap.remove(programName);
        deleteFromOtherDB(oldProgram);
        return oldProgram;
    }

    private boolean verifyProgram(final Program program)
    {
        if (program.getCourses() == null || (program.getCourses() != null
                && !program.getCourses().stream().allMatch(courseId -> courseMap.containsKey(courseId))))
        {
            return false;
        }
        else if (program.getStudents() == null || (program.getStudents() != null
                && !program.getStudents().stream().allMatch(studentId -> studentMap.containsKey(studentId))))
        {
            return false;
        }
        return true;
    }

    private void addToOtherDB(final Program program)
    {
        if (program.getCourses() != null)
        {
            program.getCourses().forEach(courseId -> courseMap.get(courseId).setProgramName(program.getProgramName()));
        }
        if (program.getStudents() != null)
        {
            program.getStudents()
                    .forEach(studentId -> studentMap.get(studentId).setProgramName(program.getProgramName()));
        }
    }

    private void deleteFromOtherDB(final Program program)
    {
        if (program.getCourses() != null)
        {
            program.getCourses().forEach(courseId -> courseMap.get(courseId).setProgramName(null));
        }
        if (program.getStudents() != null)
        {
            program.getStudents().forEach(studentId -> studentMap.get(studentId).setProgramName(null));
        }
    }

}
