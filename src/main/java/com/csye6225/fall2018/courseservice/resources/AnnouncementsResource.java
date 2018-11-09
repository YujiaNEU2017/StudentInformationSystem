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

import com.csye6225.fall2018.courseservice.datamodels.Announcement;
import com.csye6225.fall2018.courseservice.services.AnnouncementsService;

@Path("announcements")
public class AnnouncementsResource
{
    final private AnnouncementsService announcementsService = new AnnouncementsService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Announcement> getAnnouncements()
    {
        return announcementsService.getAllAnnouncements();
    }

    @GET
    @Path("/{boardId_announcementId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Announcement getAnnouncement(@PathParam("boardId_announcementId") final String boardId_announcementId)
    {
        final String[] ids = boardId_announcementId.split("_");
        if (ids.length != 2 || ids[0].isEmpty() || ids[1].isEmpty())
        {
            return null;
        }
        return announcementsService.getAnnouncement(ids[0], ids[1]);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Announcement addProfessor(final Announcement announcement)
    {
        if (announcement == null)
        {
            return null;
        }
        return announcementsService.addAnnouncement(announcement);
    }

    @PUT
    @Path("/{boardId_announcementId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Announcement updateAnnouncement(@PathParam("boardId_announcementId") final String boardId_announcementId,
            final Announcement announcement)
    {
        final String[] ids = boardId_announcementId.split("_");
        if (announcement == null || ids.length != 2 || ids[0].isEmpty() || ids[1].isEmpty())
        {
            return null;
        }
        return announcementsService.updateAnnouncement(ids[0], ids[1], announcement);
    }

    @DELETE
    @Path("/{boardId_announcementId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Announcement deleteAnnouncement(@PathParam("boardId_announcementId") final String boardId_announcementId)
    {
        final String[] ids = boardId_announcementId.split("_");
        if (ids.length != 2 || ids[0].isEmpty() || ids[1].isEmpty())
        {
            return null;
        }
        return announcementsService.deleteAnnouncement(ids[0], ids[1]);
    }

}
