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

import com.csye6225.fall2018.courseservice.datamodels.Registrar;
import com.csye6225.fall2018.courseservice.services.RegistrarsService;

@Path("registerOffering")
public class RegistrarsResource
{
    final private RegistrarsService registrarsService = new RegistrarsService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Registrar> getRegistrars()
    {
        return registrarsService.getAllRegistrars();
    }

    @GET
    @Path("/{registrarId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Registrar getregistrar(@PathParam("registrarId") final String registrarId)
    {
        return registrarsService.getRegistrar(registrarId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Registrar addRegistrar(final Registrar registrar)
    {
        if (registrar == null)
        {
            return null;
        }
        return registrarsService.addregistrar(registrar);
    }

    @PUT
    @Path("/{registrarId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Registrar updateRegistrar(@PathParam("registrarId") final String registrarId, final Registrar registrar)
    {
        if (registrar == null)
        {
            return null;
        }
        return registrarsService.updateRegistrar(registrarId, registrar);
    }

    @DELETE
    @Path("/{registrarId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Registrar deleteRegistrar(@PathParam("registrarId") final String registrarId)
    {
        return registrarsService.deleteRegistrar(registrarId);
    }

}
