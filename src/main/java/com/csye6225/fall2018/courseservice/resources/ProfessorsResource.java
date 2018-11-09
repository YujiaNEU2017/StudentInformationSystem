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

import com.csye6225.fall2018.courseservice.datamodels.Professor;
import com.csye6225.fall2018.courseservice.services.ProfessorsService;

@Path("professors")
public class ProfessorsResource
{
    final private ProfessorsService professorsService = new ProfessorsService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Professor> getProfessors()
    {
        return professorsService.getAllProfessors();
    }

    @GET
    @Path("/{professorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Professor getProfessor(@PathParam("professorId") final String professorId)
    {
        return professorsService.getProfessor(professorId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Professor addProfessor(final Professor professor)
    {
        if (professor == null)
        {
            return null;
        }
        return professorsService.addProfessor(professor);
    }

    @PUT
    @Path("/{professorId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Professor updateProfessor(@PathParam("professorId") final String professorId, final Professor professor)
    {
        if (professor == null)
        {
            return null;
        }
        return professorsService.updateProfessor(professorId, professor);
    }

    @DELETE
    @Path("/{professorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Professor deleteProfessor(@PathParam("professorId") final String professorId)
    {
        return professorsService.deleteProfessor(professorId);
    }

}
