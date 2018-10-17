package com.csye6225.fall2018.courseservice.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.csye6225.fall2018.courseservice.datamodels.Lecture;
import com.csye6225.fall2018.courseservice.services.LecturesService;

@Path("lectures")
public class LecturesResource
{
    final private LecturesService lecturesService = new LecturesService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Lecture> getLectures(@QueryParam("courseId") final String courseId)
    {
        List<Lecture> lectures = lecturesService.getAllLectures();
        if (courseId != null && !courseId.isEmpty())
        {
            lectures = lecturesService.getLectureByCourseId(lectures, courseId);
        }
        return lectures;
    }

    @GET
    @Path("/{lectureId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Lecture getLecture(@PathParam("lectureId") final long lectureId)
    {
        return lecturesService.getLecture(lectureId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Lecture addLecture(final Lecture lecture)
    {
        if (lecture == null)
        {
            return null;
        }
        return lecturesService.addLecture(lecture);
    }

    @PUT
    @Path("/{lectureId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Lecture updateLecture(@PathParam("lectureId") final long lectureId, final Lecture lecture)
    {
        if (lecture == null)
        {
            return null;
        }
        return lecturesService.updateLecture(lectureId, lecture);
    }

    @DELETE
    @Path("/{lectureId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Lecture deleteLecture(@PathParam("lectureId") final long lectureId)
    {
        return lecturesService.deleteLecture(lectureId);
    }

}
