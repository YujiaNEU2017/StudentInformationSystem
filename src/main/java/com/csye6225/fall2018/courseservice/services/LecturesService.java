package com.csye6225.fall2018.courseservice.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.csye6225.fall2018.courseservice.datamodels.Course;
import com.csye6225.fall2018.courseservice.datamodels.InMemoryDatabase;
import com.csye6225.fall2018.courseservice.datamodels.Lecture;

import jersey.repackaged.com.google.common.collect.Lists;

public class LecturesService
{
    final private static Map<Long, Lecture> lectureMap = InMemoryDatabase.getLecturesDB();
    final private static Map<String, Course> courseMap = InMemoryDatabase.getCoursesDB();

    public List<Lecture> getAllLectures()
    {
        return Lists.newArrayList(lectureMap.values());
    }

    public Lecture getLecture(final Long lectureId)
    {
        return lectureMap.get(lectureId);
    }

    public Lecture addLecture(final Lecture lecture)
    {
        if (!verifyLecture(lecture))
        {
            return null;
        }
        final long nextAvaliableId = lectureMap.size() + 1;
        lecture.setLectureId(nextAvaliableId);
        lectureMap.put(nextAvaliableId, lecture);
        addToOtherDB(lecture);
        return lectureMap.get(nextAvaliableId);
    }

    public Lecture updateLecture(final Long lectureId, final Lecture lecture)
    {
        final Lecture oldLecture = lectureMap.get(lectureId);
        if (oldLecture == null || !verifyLecture(lecture))
        {
            return null;
        }
        lecture.setLectureId(oldLecture.getLectureId());
        lectureMap.put(oldLecture.getLectureId(), lecture);
        deleteFromOtherDB(oldLecture);
        addToOtherDB(lecture);
        return lecture;
    }

    public Lecture deleteLecture(final Long lectureId)
    {
        final Lecture oldLecture = lectureMap.get(lectureId);
        if (oldLecture == null)
        {
            return null;
        }
        lectureMap.remove(lectureId);
        deleteFromOtherDB(oldLecture);
        return oldLecture;
    }

    public List<Lecture> getLectureByCourseId(final List<Lecture> lectures, final String courseId)
    {
        if (!courseMap.containsKey(courseId))
        {
            return Lists.newArrayList();
        }
        return courseMap.get(courseId).getLectures().stream().map(lectureId -> lectureMap.get(lectureId))
                .collect(Collectors.toList());
    }

    private boolean verifyLecture(final Lecture lecture)
    {
        if (lecture.getCourseId() != null && !courseMap.containsKey(lecture.getCourseId()))
        {
            return false;
        }

        return true;
    }

    private void addToOtherDB(final Lecture lecture)
    {
        if (lecture.getCourseId() != null)
        {
            courseMap.get(lecture.getCourseId()).getLectures().add(lecture.getLectureId());
        }
    }

    private void deleteFromOtherDB(final Lecture lecture)
    {
        if (lecture.getCourseId() != null)
        {
            courseMap.get(lecture.getCourseId()).getLectures().remove(lecture.getLectureId());
        }
    }
}
