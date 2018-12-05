package com.csye6225.fall2018.courseservice.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.csye6225.fall2018.courseservice.datamodels.Board;
import com.csye6225.fall2018.courseservice.datamodels.Course;
import com.csye6225.fall2018.courseservice.datamodels.DynamoDBConnector;
import com.csye6225.fall2018.courseservice.datamodels.Professor;
import com.csye6225.fall2018.courseservice.datamodels.Student;

public class CoursesService
{
    final private DynamoDBMapper dynamoDBMapper;

    public CoursesService()
    {
        dynamoDBMapper = new DynamoDBMapper(DynamoDBConnector.getClient());
    }

    public List<Course> getAllCourses()
    {
        return dynamoDBMapper.scan(Course.class, new DynamoDBScanExpression());
    }

    public Course addCourse(final Course course)
    {
        if (!verifyCourse(course) || getCourse(course.getCourseId()) != null)
        {
            return null;
        }
        course.setNotificationTopic(UtilsService.CreateTopic(course.getCourseId()));
        dynamoDBMapper.save(course);
        addToOtherDB(course, null);
        return getCourse(course.getCourseId());
    }

    public Course getCourse(final String courseId)
    {
        final List<Course> courses = dynamoDBMapper.query(Course.class,
                UtilsService.<Course> composeQueryExpression("courseId", courseId));
        if (courses == null || courses.isEmpty())
        {
            return null;
        }
        return courses.get(0);
    }

    public Course updateCourse(final String courseId, final Course course)
    {
        final Course oldCourse = getCourse(courseId);
        if (oldCourse == null || !verifyCourse(course))
        {
            return null;
        }
        course.setId(oldCourse.getId());
        course.setCourseId(oldCourse.getCourseId());
        course.setNotificationTopic(oldCourse.getNotificationTopic());
        dynamoDBMapper.save(course);
        removeFromOtherDB(oldCourse, course);
        addToOtherDB(course, oldCourse);
        return getCourse(courseId);
    }

    public Course deleteCourse(final String courseId)
    {
        final Course oldCourse = getCourse(courseId);
        if (oldCourse == null)
        {
            return null;
        }
        dynamoDBMapper.delete(oldCourse);
        UtilsService.DeleteTopic(oldCourse.getNotificationTopic());
        removeFromOtherDB(oldCourse, null);
        return oldCourse;
    }

    private boolean verifyCourse(final Course course)
    {
        course.setId(null);
        course.setNotificationTopic(null);
        if (course.getRoster() != null && course.getRoster().isEmpty())
        {
            course.setRoster(null);
        }

        if (course.getCourseId() == null || course.getCourseId().isEmpty())
        {
            return false;
        }
        if (course.getProfessorId() != null && !course.getProfessorId().isEmpty()
                && dynamoDBMapper.count(Professor.class,
                        UtilsService.<Professor> composeQueryExpression("professorId", course.getProfessorId())) != 1)
        {
            return false;
        }
        if (course.getTaId() != null && !course.getTaId().isEmpty() && dynamoDBMapper.count(Student.class,
                UtilsService.<Student> composeQueryExpression("studentId", course.getTaId())) != 1)
        {
            return false;
        }
        if (course.getBoardId() != null && !course.getBoardId().isEmpty() && dynamoDBMapper.count(Board.class,
                UtilsService.<Board> composeQueryExpression("boardId", course.getBoardId())) != 1)
        {
            return false;
        }
        if (course.getRoster() != null && !course.getRoster().isEmpty() && dynamoDBMapper.count(Student.class,
                UtilsService.composeScanExpression("studentId", course.getRoster())) != course.getRoster().size())
        {
            return false;
        }
        return true;
    }

    private void removeFromOtherDB(final Course oldCourse, final Course course)
    {
        final String oldBoardId = oldCourse.getBoardId();
        if ((oldBoardId != null && !oldBoardId.isEmpty())
                && (course == null || !oldBoardId.equals(course.getBoardId())))
        {
            final List<Board> boards = dynamoDBMapper.query(Board.class,
                    UtilsService.<Board> composeQueryExpression("boardId", oldBoardId));
            if (boards != null && !boards.isEmpty())
            {
                final Board board = boards.get(0);
                board.setCourseId(null);
                dynamoDBMapper.save(board);
            }
        }

        final Set<String> oldRoster = oldCourse.getRoster();
        if ((oldRoster != null && !oldRoster.isEmpty()) && (course == null || !oldRoster.equals(course.getRoster())))
        {
            final List<Student> students = dynamoDBMapper.scan(Student.class,
                    UtilsService.composeScanExpression("studentId", oldRoster));
            students.forEach((student) ->
            {
                if (student.getRegisteredCourses() != null)
                {
                    student.getRegisteredCourses().remove(oldCourse.getCourseId());
                    if (student.getRegisteredCourses().isEmpty())
                    {
                        student.setRegisteredCourses(null);
                    }
                }
            });
            dynamoDBMapper.batchSave(students);
        }
    }

    private void addToOtherDB(final Course course, final Course oldCourse)
    {
        final String boardId = course.getBoardId();
        if ((boardId != null && !boardId.isEmpty())
                && (oldCourse == null || !course.getBoardId().equals(oldCourse.getBoardId())))
        {
            final List<Board> boards = dynamoDBMapper.query(Board.class,
                    UtilsService.<Board> composeQueryExpression("boardId", boardId));
            if (boards != null && !boards.isEmpty())
            {
                final Board board = boards.get(0);
                board.setCourseId(course.getCourseId());
                dynamoDBMapper.save(board);
            }
        }

        final Set<String> roster = course.getRoster();
        if ((roster != null && !roster.isEmpty()) && (oldCourse == null || !roster.equals(oldCourse.getRoster())))
        {
            final List<Student> students = dynamoDBMapper.scan(Student.class,
                    UtilsService.composeScanExpression("studentId", roster));
            students.forEach(student ->
            {
                if (student.getRegisteredCourses() == null)
                {
                    student.setRegisteredCourses(new HashSet<>());
                }
                student.getRegisteredCourses().add(course.getCourseId());
            });
            dynamoDBMapper.batchSave(students);
        }
    }
}
