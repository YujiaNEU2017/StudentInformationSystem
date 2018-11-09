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

import com.csye6225.fall2018.courseservice.datamodels.Board;
import com.csye6225.fall2018.courseservice.services.BoardsService;

@Path("boards")
public class BoardsResource
{
    final private BoardsService boardsService = new BoardsService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Board> getBoards()
    {
        return boardsService.getAllBoards();
    }

    @GET
    @Path("/{boardId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Board getBoard(@PathParam("boardId") final String boardId)
    {
        return boardsService.getBoard(boardId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Board addBoard(final Board board)
    {
        if (board == null)
        {
            return null;
        }
        return boardsService.addBoard(board);
    }

    @PUT
    @Path("/{boardId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Board updateBoard(@PathParam("boardId") final String boardId, final Board board)
    {
        if (board == null)
        {
            return null;
        }
        return boardsService.updateBoard(boardId, board);
    }

    @DELETE
    @Path("/{boardId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Board deleteBoard(@PathParam("boardId") final String boardId)
    {
        return boardsService.deleteBoard(boardId);
    }

}
