package com.csye6225.fall2018.courseservice.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.csye6225.fall2018.courseservice.datamodels.Course;
import com.csye6225.fall2018.courseservice.datamodels.InMemoryDatabase;
import com.csye6225.fall2018.courseservice.datamodels.Program;
import com.csye6225.fall2018.courseservice.datamodels.Student;

import jersey.repackaged.com.google.common.collect.Lists;

public class StudentsService
{
    final private static Map<Long, Student> studentMap = InMemoryDatabase.getStudentsDB();
    final private static Map<String, Course> courseMap = InMemoryDatabase.getCoursesDB();
    final private static Map<String, Program> programMap = InMemoryDatabase.getProgramsDB();

    public List<Student> getAllStudents()
    {
        return Lists.newArrayList(studentMap.values());
    }

    public Student addStudent(final Student student)
    {
        if (!verifyStudent(student))
        {
            return null;
        }
        final long nextAvaliableId = studentMap.size() + 1;
        student.setStudentId(nextAvaliableId);
        studentMap.put(nextAvaliableId, student);
        addToOtherDB(student);
        return studentMap.get(nextAvaliableId);
    }

    public Student getStudent(final long studentId)
    {
        return studentMap.get(studentId);
    }

    public Student updateStudent(final long studentId, final Student student)
    {
        final Student oldStudent = studentMap.get(studentId);
        if (oldStudent == null || !verifyStudent(student))
        {
            return null;
        }
        student.setStudentId(oldStudent.getStudentId());
        studentMap.put(oldStudent.getStudentId(), student);
        deleteFromOtherDB(oldStudent);
        addToOtherDB(student);
        return student;
    }

    public Student deleteStudent(final long studentId)
    {
        final Student oldStudent = studentMap.get(studentId);
        if (oldStudent == null)
        {
            return null;
        }
        studentMap.remove(studentId);
        deleteFromOtherDB(oldStudent);
        return oldStudent;
    }

    public List<Student> getStudentsByCourseId(final List<Student> students, final String courseId)
    {
        if (!courseMap.containsKey(courseId))
        {
            return Lists.newArrayList();
        }
        return courseMap.get(courseId).getRoster().stream().map(studentId -> studentMap.get(studentId))
                .collect(Collectors.toList());
    }

    public List<Student> getStudentsByProgramName(final List<Student> students, final String programName)
    {
        if (!programMap.containsKey(programName))
        {
            return Lists.newArrayList();
        }
        return programMap.get(programName).getStudents().stream().map(studentId -> studentMap.get(studentId))
                .collect(Collectors.toList());
    }

    private boolean verifyStudent(final Student student)
    {
        if (student.getProgramName() != null && !programMap.containsKey(student.getProgramName()))
        {
            return false;
        }
        else if (student.getCoursesEnrolled() == null || (student.getCoursesEnrolled() != null
                && !student.getCoursesEnrolled().stream().allMatch(courseId -> courseMap.containsKey(courseId))))
        {
            return false;
        }
        return true;
    }

    private void addToOtherDB(final Student student)
    {
        if (student.getCoursesEnrolled() != null)
        {
            student.getCoursesEnrolled()
                    .forEach(courseId -> courseMap.get(courseId).getRoster().add(student.getStudentId()));
        }
        if (student.getCoursesAssisted() != null)
        {
            student.getCoursesAssisted()
                    .forEach(courseId -> courseMap.get(courseId).setStudentTAId(student.getStudentId()));
        }
        if (student.getProgramName() != null)
        {
            programMap.get(student.getProgramName()).getStudents().add(student.getStudentId());
        }
    }

    private void deleteFromOtherDB(final Student student)
    {
        if (student.getCoursesEnrolled() != null)
        {
            student.getCoursesEnrolled()
                    .forEach(courseId -> courseMap.get(courseId).getRoster().remove(student.getStudentId()));
        }
        if (student.getCoursesAssisted() != null)
        {
            student.getCoursesAssisted().forEach(courseId -> courseMap.get(courseId).setStudentTAId(0));
        }
        if (student.getProgramName() != null)
        {
            programMap.get(student.getProgramName()).getStudents().remove(student.getStudentId());
        }
    }
}
