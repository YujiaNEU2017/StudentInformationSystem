package com.csye6225.fall2018.courseservice.services;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.csye6225.fall2018.courseservice.datamodels.Announcement;
import com.csye6225.fall2018.courseservice.datamodels.Board;
import com.csye6225.fall2018.courseservice.datamodels.Course;
import com.csye6225.fall2018.courseservice.datamodels.DynamoDBConnector;

public class BoardsService
{
    final private DynamoDBMapper dynamoDBMapper;

    public BoardsService()
    {
        dynamoDBMapper = new DynamoDBMapper(DynamoDBConnector.getClient());
    }

    public List<Board> getAllBoards()
    {
        return dynamoDBMapper.scan(Board.class, new DynamoDBScanExpression());
    }

    public Board addBoard(final Board board)
    {
        if (!verifyBoard(board) || getBoard(board.getBoardId()) != null)
        {
            return null;
        }
        dynamoDBMapper.save(board);
        addToOtherDB(board, null);
        return getBoard(board.getBoardId());
    }

    public Board getBoard(final String boardId)
    {
        final List<Board> boards = dynamoDBMapper.query(Board.class,
                UtilsService.<Board> composeQueryExpression("boardId", boardId));
        if (boards == null || boards.isEmpty())
        {
            return null;
        }
        return boards.get(0);
    }

    public Board updateBoard(final String boardId, final Board board)
    {
        final Board oldBoard = getBoard(boardId);
        if (oldBoard == null || !verifyBoard(board))
        {
            return null;
        }
        board.setId(oldBoard.getId());
        board.setBoardId(oldBoard.getBoardId());
        dynamoDBMapper.save(board);
        removeFromOtherDB(oldBoard, board);
        addToOtherDB(board, oldBoard);
        return getBoard(boardId);
    }

    public Board deleteBoard(final String boardId)
    {
        final Board oldBoard = getBoard(boardId);
        if (oldBoard == null)
        {
            return null;
        }
        dynamoDBMapper.delete(oldBoard);
        removeFromOtherDB(oldBoard, null);
        return oldBoard;
    }

    private boolean verifyBoard(final Board board)
    {
        board.setId(null);
        if (board.getBoardId() == null)
        {
            return false;
        }
        if (board.getCourseId() != null && !board.getCourseId().isEmpty() && dynamoDBMapper.count(Course.class,
                UtilsService.<Course> composeQueryExpression("courseId", board.getCourseId())) != 1)
        {
            return false;
        }
        return true;
    }

    private void removeFromOtherDB(final Board oldBoard, final Board board)
    {
        final String oldBoardId = oldBoard.getBoardId();
        if (board == null)
        {
            final List<Announcement> announcements = dynamoDBMapper.scan(Announcement.class,
                    UtilsService.composeScanExpression("boardId", oldBoardId));
            if (announcements != null && !announcements.isEmpty())
            {
                dynamoDBMapper.batchDelete(announcements);
            }
        }

        final String oldCourseId = oldBoard.getCourseId();
        if ((oldCourseId != null && !oldCourseId.isEmpty()) && (board == null || !oldCourseId.equals(board.getCourseId())))
        {
            final List<Course> courses = dynamoDBMapper.query(Course.class,
                    UtilsService.<Course> composeQueryExpression("courseId", oldCourseId));
            if (courses != null && !courses.isEmpty())
            {
                final Course course = courses.get(0);
                course.setBoardId(null);
                dynamoDBMapper.save(course);
            }
        }
    }

    private void addToOtherDB(final Board board, final Board oldBoard)
    {
        final String courseId = board.getCourseId();
        if ((courseId != null && !courseId.isEmpty()) && (oldBoard == null || !courseId.equals(oldBoard.getCourseId())))
        {
            final List<Course> courses = dynamoDBMapper.query(Course.class,
                    UtilsService.<Course> composeQueryExpression("courseId", courseId));
            if (courses != null && !courses.isEmpty())
            {
                final Course course = courses.get(0);
                course.setBoardId(board.getBoardId());
                dynamoDBMapper.save(course);
            }
        }
    }
}
