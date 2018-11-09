package com.csye6225.fall2018.courseservice.services;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.csye6225.fall2018.courseservice.datamodels.Announcement;
import com.csye6225.fall2018.courseservice.datamodels.Board;
import com.csye6225.fall2018.courseservice.datamodels.DynamoDBConnector;

public class AnnouncementsService
{
    final private DynamoDBMapper dynamoDBMapper;

    public AnnouncementsService()
    {
        dynamoDBMapper = new DynamoDBMapper(DynamoDBConnector.getClient());
    }

    public List<Announcement> getAllAnnouncements()
    {
        return dynamoDBMapper.scan(Announcement.class, new DynamoDBScanExpression());
    }

    public Announcement addAnnouncement(final Announcement announcement)
    {
        if (!verifyAnnouncement(announcement)
                || getAnnouncement(announcement.getBoardId(), announcement.getAnnouncementId()) != null)
        {
            return null;
        }
        dynamoDBMapper.save(announcement);
        return getAnnouncement(announcement.getBoardId(), announcement.getAnnouncementId());
    }

    public Announcement getAnnouncement(final String boardId, final String announcementId)
    {
        final List<Announcement> announcements = dynamoDBMapper.query(Announcement.class,
                UtilsService.<Announcement> composeQueryExpression("boardId", boardId, "announcementId", announcementId));
        if (announcements == null || announcements.isEmpty())
        {
            return null;
        }
        return announcements.get(0);
    }

    public Announcement updateAnnouncement(final String boardId, final String announcementId, final Announcement announcement)
    {
        final Announcement oldAnnouncement = getAnnouncement(boardId, announcementId);
        if (oldAnnouncement == null || !verifyAnnouncement(announcement))
        {
            return null;
        }
        announcement.setId(oldAnnouncement.getId());
        announcement.setBoardId(oldAnnouncement.getBoardId());
        announcement.setAnnouncementId(oldAnnouncement.getAnnouncementId());
        dynamoDBMapper.save(announcement);
        return getAnnouncement(boardId, announcementId);
    }

    public Announcement deleteAnnouncement(final String boardId, final String announcementId)
    {
        final Announcement oldAnnouncement = getAnnouncement(boardId, announcementId);
        if (oldAnnouncement == null)
        {
            return null;
        }
        dynamoDBMapper.delete(oldAnnouncement);
        return oldAnnouncement;
    }

    private boolean verifyAnnouncement(final Announcement announcement)
    {
        announcement.setId(null);
        if (announcement.getAnnouncementId() == null || announcement.getAnnouncementId().isEmpty())
        {
            return false;
        }
        if (announcement.getBoardId() == null || announcement.getBoardId().isEmpty() || dynamoDBMapper.count(Board.class,
                UtilsService.<Board> composeQueryExpression("boardId", announcement.getBoardId())) != 1)
        {
            return false;
        }
        if (announcement.getAnnouncementText() != null && announcement.getAnnouncementText().length() > 160)
        {
            return false;
        }
        return true;
    }

}
