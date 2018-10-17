package com.csye6225.fall2018.courseservice.services;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.csye6225.fall2018.courseservice.datamodels.Course;
import com.csye6225.fall2018.courseservice.datamodels.InMemoryDatabase;
import com.csye6225.fall2018.courseservice.datamodels.Lecture;
import com.csye6225.fall2018.courseservice.datamodels.Professor;
import com.csye6225.fall2018.courseservice.datamodels.Program;
import com.csye6225.fall2018.courseservice.datamodels.Student;

import jersey.repackaged.com.google.common.collect.Lists;

public class CoursesService
{
    final private static Map<String, Course> courseMap = InMemoryDatabase.getCoursesDB();
    final private static Map<Long, Lecture> lectureMap = InMemoryDatabase.getLecturesDB();
    final private static Map<String, Program> programMap = InMemoryDatabase.getProgramsDB();
    final private static Map<Long, Student> studentMap = InMemoryDatabase.getStudentsDB();
    final private static Map<Long, Professor> professorMap = InMemoryDatabase.getProfessorsDB();

    public List<Course> getAllCourses()
    {
        return Lists.newArrayList(courseMap.values());
    }

    public Course getCourse(final String courseId)
    {
        return courseMap.get(courseId);
    }

    public Course addCourse(final Course course)
    {
        final String courseId = course.getCourseId();
        if (courseMap.containsKey(courseId) || !verifyCourse(course))
        {
            return null;
        }
        courseMap.put(courseId, course);
        addToOtherDB(course);
        return courseMap.get(courseId);
    }

    public Course updateCourse(final String courseId, final Course course)
    {
        final Course oldCourse = courseMap.get(courseId);
        if (oldCourse == null || !verifyCourse(course))
        {
            return null;
        }
        course.setCourseId(oldCourse.getCourseId());
        courseMap.put(oldCourse.getCourseId(), course);
        deleteFromOtherDB(oldCourse);
        addToOtherDB(course);
        return course;
    }

    public Course deleteCourse(final String courseId)
    {
        final Course oldCourse = courseMap.get(courseId);
        if (oldCourse == null)
        {
            return null;
        }
        courseMap.remove(courseId);
        deleteFromOtherDB(oldCourse);
        return oldCourse;
    }

    public List<Course> getCourseByProgramName(final List<Course> courses, final String programName)
    {
        if (!programMap.containsKey(programName))
        {
            return Lists.newArrayList();
        }
        return programMap.get(programName).getCourses().stream().map(courseId -> courseMap.get(courseId))
                .collect(Collectors.toList());
    }

    public String getBoard(final String courseId)
    {
        final Course course = courseMap.get(courseId);
        if (course == null)
        {
            return null;
        }
        return course.getBoard();
    }

    public String updateBoard(final String courseId, final String board)
    {
        final Course course = courseMap.get(courseId);
        if (course == null)
        {
            return null;
        }
        course.setBoard(board);
        return course.getBoard();
    }

    public String getRoster(final String courseId)
    {
        final Course course = courseMap.get(courseId);
        if (course == null)
        {
            return null;
        }
        return course.getRoster().stream().map(studentId -> String.valueOf(studentId))
                .reduce((studentId1, studentId2) -> studentId1 + "," + studentId2).orElse(null);
    }

    public String updateRoster(final String courseId, final Set<Long> roster)
    {
        final Course course = courseMap.get(courseId);
        if (course == null || !verifyRoster(roster))
        {
            return null;
        }
        deleteRosterFromOtherDB(course);
        course.setRoster(roster);
        addRosterToOtherDB(course);
        return course.getRoster().stream().map(studentId -> String.valueOf(studentId))
                .reduce((studentId1, studentId2) -> studentId1 + "," + studentId2).orElse(null);
    }

    private boolean verifyCourse(final Course course)
    {
        if (course.getProgramName() != null && !programMap.containsKey(course.getProgramName()))
        {
            return false;
        }
        else if (course.getProfessorId() != 0 && !professorMap.containsKey(course.getProfessorId()))
        {
            return false;
        }
        else if (course.getStudentTAId() != 0 && !studentMap.containsKey(course.getStudentTAId()))
        {
            return false;
        }
        else if (course.getLectures() == null || (course.getLectures() != null
                && !course.getLectures().stream().allMatch(lectureId -> lectureMap.containsKey(lectureId))))
        {
            return false;
        }
        else if (course.getRoster() == null || (course.getRoster() != null
                && !course.getRoster().stream().allMatch(studentId -> studentMap.containsKey(studentId))))
        {
            return false;
        }
        return true;
    }

    private void addToOtherDB(final Course course)
    {
        if (course.getProgramName() != null)
        {
            programMap.get(course.getProgramName()).getCourses().add(course.getCourseId());
        }
        if (course.getProfessorId() != 0)
        {
            professorMap.get(course.getProfessorId()).getCoursesTaught().add(course.getCourseId());
        }
        if (course.getStudentTAId() != 0)
        {
            studentMap.get(course.getStudentTAId()).getCoursesAssisted().add(course.getCourseId());
        }
        if (course.getLectures() != null)
        {
            course.getLectures().forEach(lectureId -> lectureMap.get(lectureId).setCourseId(course.getCourseId()));
        }
        if (course.getRoster() != null)
        {
            course.getRoster()
                    .forEach(studentId -> studentMap.get(studentId).getCoursesEnrolled().add(course.getCourseId()));
        }
    }

    private void deleteFromOtherDB(final Course course)
    {
        if (course.getProgramName() != null)
        {
            programMap.get(course.getProgramName()).getCourses().remove(course.getCourseId());
        }
        if (course.getProfessorId() != 0)
        {
            professorMap.get(course.getProfessorId()).getCoursesTaught().remove(course.getCourseId());
        }
        if (course.getStudentTAId() != 0)
        {
            studentMap.get(course.getStudentTAId()).getCoursesAssisted().remove(course.getCourseId());
        }
        if (course.getLectures() != null)
        {
            course.getLectures().forEach(lectureId -> lectureMap.get(lectureId).setCourseId(null));
        }
        if (course.getRoster() != null)
        {
            course.getRoster()
                    .forEach(studentId -> studentMap.get(studentId).getCoursesEnrolled().remove(course.getCourseId()));
        }
    }

    private boolean verifyRoster(final Set<Long> roster)
    {
        if (roster == null
                || (roster != null && !roster.stream().allMatch(studentId -> studentMap.containsKey(studentId))))
        {
            return false;
        }
        return true;
    }

    private void addRosterToOtherDB(final Course course)
    {
        if (course.getRoster() != null)
        {
            course.getRoster()
                    .forEach(studentId -> studentMap.get(studentId).getCoursesEnrolled().add(course.getCourseId()));
        }
    }

    private void deleteRosterFromOtherDB(final Course course)
    {
        if (course.getRoster() != null)
        {
            course.getRoster()
                    .forEach(studentId -> studentMap.get(studentId).getCoursesEnrolled().remove(course.getCourseId()));
        }
    }

}
