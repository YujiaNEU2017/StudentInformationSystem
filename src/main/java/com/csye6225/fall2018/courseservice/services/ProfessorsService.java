package com.csye6225.fall2018.courseservice.services;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.csye6225.fall2018.courseservice.datamodels.Course;
import com.csye6225.fall2018.courseservice.datamodels.DynamoDBConnector;
import com.csye6225.fall2018.courseservice.datamodels.Professor;

public class ProfessorsService
{
    final private DynamoDBMapper dynamoDBMapper;

    public ProfessorsService()
    {
        dynamoDBMapper = new DynamoDBMapper(DynamoDBConnector.getClient());
    }

    public List<Professor> getAllProfessors()
    {
        return dynamoDBMapper.scan(Professor.class, new DynamoDBScanExpression());
    }

    public Professor addProfessor(final Professor professor)
    {
        if (!verifyProfessor(professor) || getProfessor(professor.getProfessorId()) != null)
        {
            return null;
        }
        dynamoDBMapper.save(professor);
        return getProfessor(professor.getProfessorId());
    }

    public Professor getProfessor(final String professorId)
    {
        final List<Professor> professors = dynamoDBMapper.query(Professor.class,
                UtilsService.<Professor> composeQueryExpression("professorId", professorId));
        if (professors == null || professors.isEmpty())
        {
            return null;
        }
        return professors.get(0);
    }

    public Professor updateProfessor(final String professorId, final Professor professor)
    {
        final Professor oldProfessor = getProfessor(professorId);
        if (oldProfessor == null || !verifyProfessor(professor))
        {
            return null;
        }
        professor.setId(oldProfessor.getId());
        professor.setProfessorId(oldProfessor.getProfessorId());
        dynamoDBMapper.save(professor);
        return getProfessor(professorId);
    }

    public Professor deleteProfessor(final String professorId)
    {
        final Professor oldProfessor = getProfessor(professorId);
        if (oldProfessor == null)
        {
            return null;
        }
        dynamoDBMapper.delete(oldProfessor);
        removeFromOtherDB(oldProfessor);
        return oldProfessor;
    }

    private boolean verifyProfessor(final Professor professor)
    {
        professor.setId(null);
        if (professor.getProfessorId() == null || professor.getProfessorId().isEmpty())
        {
            return false;
        }
        return true;
    }

    private void removeFromOtherDB(final Professor professor)
    {
        final String oldProfessorId = professor.getProfessorId();
        final List<Course> courses = dynamoDBMapper.scan(Course.class,
                UtilsService.composeScanExpression("professorId", oldProfessorId));
        if (courses != null && !courses.isEmpty())
        {
            courses.forEach(course -> course.setProfessorId(null));
            dynamoDBMapper.batchSave(courses);
        }
    }

}
