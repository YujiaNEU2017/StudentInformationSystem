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
import javax.ws.rs.core.MediaType;

import com.csye6225.fall2018.courseservice.datamodels.Course;
import com.csye6225.fall2018.courseservice.services.CoursesService;

@Path("courses")
public class CoursesResource
{
    final private CoursesService coursesService = new CoursesService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Course> getCourses()
    {
        return coursesService.getAllCourses();
    }

    @GET
    @Path("/{courseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Course getCourse(@PathParam("courseId") final String courseId)
    {
        return coursesService.getCourse(courseId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Course addCourse(final Course course)
    {
        if (course == null)
        {
            return null;
        }
        return coursesService.addCourse(course);
    }

    @PUT
    @Path("/{courseId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Course updateCourse(@PathParam("courseId") final String courseId, final Course course)
    {
        if (course == null)
        {
            return null;
        }
        return coursesService.updateCourse(courseId, course);
    }

    @DELETE
    @Path("/{courseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Course deleteCourse(@PathParam("courseId") final String courseId)
    {
        return coursesService.deleteCourse(courseId);
    }

}
