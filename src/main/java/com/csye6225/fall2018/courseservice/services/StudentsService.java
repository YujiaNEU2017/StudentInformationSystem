package com.csye6225.fall2018.courseservice.services;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.csye6225.fall2018.courseservice.datamodels.Course;
import com.csye6225.fall2018.courseservice.datamodels.DynamoDBConnector;
import com.csye6225.fall2018.courseservice.datamodels.Student;

import jersey.repackaged.com.google.common.collect.Sets;

public class StudentsService
{
    final private DynamoDBMapper dynamoDBMapper;

    public StudentsService()
    {
        dynamoDBMapper = new DynamoDBMapper(DynamoDBConnector.getClient());
    }

    public List<Student> getAllStudents()
    {
        return dynamoDBMapper.scan(Student.class, new DynamoDBScanExpression());
    }

    public Student addStudent(final Student student)
    {
        if (!verifyStudent(student) || getStudent(student.getStudentId()) != null)
        {
            return null;
        }
        subscribeTopics(student.getRegisteredCourses(), student.getEmailId());
        dynamoDBMapper.save(student);
        addToOtherDB(student, null);
        return getStudent(student.getStudentId());
    }

    public Student getStudent(final String studentId)
    {
        final List<Student> students = dynamoDBMapper.query(Student.class,
                UtilsService.<Student> composeQueryExpression("studentId", studentId));
        if (students == null || students.isEmpty())
        {
            return null;
        }
        return students.get(0);
    }

    public Student updateStudent(final String studentId, final Student student)
    {
        final Student oldStudent = getStudent(studentId);
        if (oldStudent == null || !verifyStudent(student))
        {
            return null;
        }
        student.setId(oldStudent.getId());
        student.setStudentId(oldStudent.getStudentId());
        dynamoDBMapper.save(student);
        removeFromOtherDB(oldStudent, student);
        addToOtherDB(student, oldStudent);
        processSNSUpdate(student, oldStudent);
        return getStudent(studentId);
    }

    public Student deleteStudent(final String studentId)
    {
        final Student oldStudent = getStudent(studentId);
        if (oldStudent == null)
        {
            return null;
        }
        dynamoDBMapper.delete(oldStudent);
        removeFromOtherDB(oldStudent, null);
        return oldStudent;
    }

    public Student registerCourses(final String studentId, final Set<String> coursesRegistration)
    {
        final Student student = getStudent(studentId);
        if (student == null || !verifyCoursesRegistration(student, coursesRegistration))
        {
            return null;
        }

        if (student.getRegisteredCourses() == null)
        {
            student.setRegisteredCourses(coursesRegistration);
        }
        else
        {
            student.getRegisteredCourses().addAll(coursesRegistration);
        }
        dynamoDBMapper.save(student);
        addToOtherDB(student, null);
        subscribeTopics(coursesRegistration, student.getEmailId());
        return student;
    }

    private boolean verifyCoursesRegistration(final Student student, final Set<String> coursesRegistration)
    {
        final Set<String> registeredCourses = student.getRegisteredCourses() != null ? student.getRegisteredCourses()
                : Collections.emptySet();
        coursesRegistration.removeAll(registeredCourses);
        if (coursesRegistration.isEmpty())
        {
            return false;
        }
        else if (student.getRegisteredCourses() != null
                && (student.getRegisteredCourses().size() + coursesRegistration.size()) > 3)
        {
            return false;
        }

        if (dynamoDBMapper.count(Course.class,
                UtilsService.composeScanExpression("courseId", coursesRegistration)) != coursesRegistration.size())
        {
            return false;
        }
        return true;
    }

    private boolean verifyStudent(final Student student)
    {
        student.setId(null);
        if (student.getRegisteredCourses() != null && student.getRegisteredCourses().isEmpty())
        {
            student.setRegisteredCourses(null);
        }
        else if (student.getRegisteredCourses() != null && student.getRegisteredCourses().size() > 3)
        {
            return false;
        }

        if (student.getEmailId() == null || student.getEmailId().isEmpty())
        {
            return false;
        }

        if (student.getStudentId() == null || student.getStudentId().isEmpty())
        {
            return false;
        }

        final Set<String> registeredCourses = student.getRegisteredCourses();
        if (registeredCourses != null && !registeredCourses.isEmpty() && dynamoDBMapper.count(Course.class,
                UtilsService.composeScanExpression("courseId", registeredCourses)) != registeredCourses.size())
        {
            return false;
        }
        return true;
    }

    private void removeFromOtherDB(final Student oldStudent, final Student student)
    {
        final String oldTaId = oldStudent.getStudentId();
        if (student == null)
        {
            final List<Course> courses = dynamoDBMapper.scan(Course.class,
                    UtilsService.composeScanExpression("taId", oldTaId));
            if (courses != null && !courses.isEmpty())
            {
                courses.forEach(course -> course.setTaId(null));
                dynamoDBMapper.batchSave(courses);
            }
        }

        final Set<String> oldRegisteredCourses = oldStudent.getRegisteredCourses();
        if ((oldRegisteredCourses != null && !oldRegisteredCourses.isEmpty())
                && (student == null || !oldRegisteredCourses.equals(student.getRegisteredCourses())))
        {
            final List<Course> courses = dynamoDBMapper.scan(Course.class,
                    UtilsService.composeScanExpression("courseId", oldRegisteredCourses));
            courses.forEach(course ->
            {
                if (course.getRoster() != null)
                {
                    course.getRoster().remove(oldStudent.getStudentId());
                    if (course.getRoster().isEmpty())
                    {
                        course.setRoster(null);
                    }
                }
            });
            dynamoDBMapper.batchSave(courses);
        }
    }

    private void addToOtherDB(final Student student, final Student oldStudent)
    {
        final Set<String> registeredCourses = student.getRegisteredCourses();
        if ((registeredCourses != null && !registeredCourses.isEmpty())
                && (oldStudent == null || !registeredCourses.equals(oldStudent.getRegisteredCourses())))
        {
            final List<Course> courses = dynamoDBMapper.scan(Course.class,
                    UtilsService.composeScanExpression("courseId", registeredCourses));
            courses.forEach(course ->
            {
                if (course.getRoster() == null)
                {
                    course.setRoster(new HashSet<>());
                }
                course.getRoster().add(student.getStudentId());
            });
            dynamoDBMapper.batchSave(courses);
        }
    }

    private void processSNSUpdate(final Student student, final Student oldStudent)
    {
        final Set<String> newRegisteredCourses = student.getRegisteredCourses() != null ? student.getRegisteredCourses()
                : Collections.emptySet();
        final Set<String> oldRegisteredCourses = oldStudent.getRegisteredCourses() != null
                ? oldStudent.getRegisteredCourses()
                : Collections.emptySet();
        if (newRegisteredCourses.isEmpty() && oldRegisteredCourses.isEmpty())
        {
            return;
        }
        else if (newRegisteredCourses.equals(oldRegisteredCourses))
        {
            return;
        }

        if (!student.getEmailId().equals(oldStudent.getEmailId()))
        {
            subscribeTopics(newRegisteredCourses, student.getEmailId());
        }
        subscribeTopics(Sets.difference(newRegisteredCourses, oldRegisteredCourses), student.getEmailId());
    }

    private void subscribeTopics(final Set<String> newRegisteredCourses, final String email)
    {
        if (newRegisteredCourses == null || newRegisteredCourses.isEmpty())
        {
            return;
        }
        newRegisteredCourses.forEach(courseId ->
        {
            final List<Course> courses = dynamoDBMapper.query(Course.class,
                    UtilsService.<Course> composeQueryExpression("courseId", courseId));
            UtilsService.SubscribeTopic(courses.get(0).getNotificationTopic(), email);
        });
    }
}
