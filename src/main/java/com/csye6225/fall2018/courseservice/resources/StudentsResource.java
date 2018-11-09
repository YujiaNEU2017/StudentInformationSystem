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

import com.csye6225.fall2018.courseservice.datamodels.Student;
import com.csye6225.fall2018.courseservice.services.StudentsService;

@Path("students")
public class StudentsResource
{
    final private StudentsService studentsService = new StudentsService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> getStudents()
    {
        return studentsService.getAllStudents();
    }

    @GET
    @Path("/{studentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Student getStudent(@PathParam("studentId") final String studentId)
    {
        return studentsService.getStudent(studentId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Student addStudent(final Student student)
    {
        if (student == null)
        {
            return null;
        }
        return studentsService.addStudent(student);
    }

    @PUT
    @Path("/{studentId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Student updateStudent(@PathParam("studentId") final String studentId, final Student student)
    {
        if (student == null)
        {
            return null;
        }
        return studentsService.updateStudent(studentId, student);
    }

    @DELETE
    @Path("/{studentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Student deleteStudent(@PathParam("studentId") final String studentId)
    {
        return studentsService.deleteStudent(studentId);
    }
}
