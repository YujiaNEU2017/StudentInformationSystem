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

import com.csye6225.fall2018.courseservice.datamodels.Program;
import com.csye6225.fall2018.courseservice.services.ProgramsService;

@Path("programs")
public class ProgramsResource
{
    final private ProgramsService programsService = new ProgramsService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Program> getAllPrograms()
    {
        return programsService.getAllPrograms();
    }

    @GET
    @Path("/{programName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Program getProgram(@PathParam("programName") final String programName)
    {
        return programsService.getProgram(programName);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Program addProgram(final Program program)
    {
        if (program == null)
        {
            return null;
        }

        return programsService.addProgram(program);

    }

    @PUT
    @Path("/{programName}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Program updateProgram(@PathParam("programName") final String programName, final Program program)
    {
        if (program == null)
        {
            return null;
        }
        return programsService.updateProgram(programName, program);
    }

    @DELETE
    @Path("/{programName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Program deleteProgram(@PathParam("programName") final String programName)
    {
        return programsService.deleteProgram(programName);
    }
}
