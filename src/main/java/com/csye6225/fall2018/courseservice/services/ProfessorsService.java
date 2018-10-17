package com.csye6225.fall2018.courseservice.services;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.csye6225.fall2018.courseservice.datamodels.Course;
import com.csye6225.fall2018.courseservice.datamodels.InMemoryDatabase;
import com.csye6225.fall2018.courseservice.datamodels.Professor;

import jersey.repackaged.com.google.common.collect.Lists;

public class ProfessorsService
{
    final private static Map<Long, Professor> professorMap = InMemoryDatabase.getProfessorsDB();
    final private static Map<String, Course> courseMap = InMemoryDatabase.getCoursesDB();

    public List<Professor> getAllProfessors()
    {
        return Lists.newArrayList(professorMap.values());
    }

    public Professor addProfessor(final Professor professor)
    {
        if (!verifyProfessor(professor))
        {
            return null;
        }
        final long nextAvaliableId = professorMap.size() + 1;
        professor.setProfessorId(nextAvaliableId);
        professorMap.put(nextAvaliableId, professor);
        addToOtherDB(professor);
        return professorMap.get(nextAvaliableId);
    }

    public Professor getProfessor(final long professorId)
    {
        return professorMap.get(professorId);
    }

    public Professor updateProfessor(final long professorId, final Professor professor)
    {
        final Professor oldProfessor = professorMap.get(professorId);
        if (oldProfessor == null || !verifyProfessor(professor))
        {
            return null;
        }
        professor.setProfessorId(oldProfessor.getProfessorId());
        professorMap.put(oldProfessor.getProfessorId(), professor);
        deleteFromOtherDB(oldProfessor);
        addToOtherDB(professor);
        return professor;
    }

    public Professor deleteProfessor(final long professorId)
    {
        final Professor oldProfessor = professorMap.get(professorId);
        if (oldProfessor == null)
        {
            return null;
        }
        professorMap.remove(professorId);
        deleteFromOtherDB(oldProfessor);
        return oldProfessor;
    }

    public List<Professor> getProfessorsByDepartment(final List<Professor> professors, final String department)
    {
        return professors.stream().filter(professor -> professor.getDepartment().equals(department))
                .collect(Collectors.toList());
    }

    public List<Professor> getProfessorsByYear(final List<Professor> professors, final int year)
    {
        return professors.stream().filter(professor -> professor.getJoiningDate().getYear() == (year - 1900))
                .collect(Collectors.toList());
    }

    public List<Professor> getProfessorsBySize(final List<Professor> professors, final int size)
    {
        return professors.stream().sorted(Comparator.comparing(Professor::getJoiningDate)).collect(Collectors.toList())
                .subList(0, Math.min(size, professors.size()));
    }

    private boolean verifyProfessor(final Professor professor)
    {
        if (professor.getCoursesTaught() == null || (professor.getCoursesTaught() != null
                && !professor.getCoursesTaught().stream().allMatch(courseId -> courseMap.containsKey(courseId))))
        {
            return false;
        }

        return true;
    }

    private void addToOtherDB(final Professor professor)
    {
        if (professor.getCoursesTaught() != null)
        {
            professor.getCoursesTaught()
                    .forEach(courseId -> courseMap.get(courseId).setProfessorId(professor.getProfessorId()));
        }
    }

    private void deleteFromOtherDB(final Professor professor)
    {
        if (professor.getCoursesTaught() != null)
        {
            professor.getCoursesTaught().forEach(courseId -> courseMap.get(courseId).setProfessorId(0));
        }
    }
}
