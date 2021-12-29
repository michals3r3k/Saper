package dev.michals3r3k.context;

import dev.michals3r3k.model.board.Board;

public class BoardContext
{
    public static BoardContext getUserContext(Context context)
    {
        Object parameter = context.getParameter(BoardContext.class);
        if(parameter != null)
        {
            return (BoardContext) parameter;
        }
        return new BoardContext();
    }

    private Board board;

    public BoardContext()
    {
        this.board = null;
    }

    public Board getBoard()
    {
        return board;
    }

    public void setBoard(final Board board)
    {
        this.board = board;
    }

}
