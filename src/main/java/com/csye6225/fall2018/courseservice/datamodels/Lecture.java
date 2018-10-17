package com.csye6225.fall2018.courseservice.datamodels;

public class Lecture
{
    private long lectureId;
    private String notes;
    private String material;
    private String courseId;

    public Lecture()
    {

    }

    public long getLectureId()
    {
        return lectureId;
    }

    public void setLectureId(long lectureId)
    {
        this.lectureId = lectureId;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public String getMaterial()
    {
        return material;
    }

    public void setMaterial(String material)
    {
        this.material = material;
    }

    public String getCourseId()
    {
        return courseId;
    }

    public void setCourseId(String courseId)
    {
        this.courseId = courseId;
    }

}
