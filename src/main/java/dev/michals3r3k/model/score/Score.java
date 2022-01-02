package dev.michals3r3k.model.score;

import dev.michals3r3k.model.Saveable;
import dev.michals3r3k.model.board.Board;
import dev.michals3r3k.model.save.GameTime;

import java.time.LocalDateTime;

public class Score implements Saveable<Long>
{
    private Long id;
    private LocalDateTime date;
    private String username;
    private GameTime gameTime;
    private Board board;

    public Score(
        LocalDateTime date,
        String username,
        GameTime gameTime,
        Board board)
    {
        this(null, date, username, gameTime, board);
    }

    public Score(
        Long id,
        LocalDateTime date,
        String username,
        GameTime gameTime,
        Board board)
    {
        this.id = id;
        this.date = date;
        this.username = username;
        this.gameTime = gameTime;
        this.board = board;
    }

    public LocalDateTime getDate()
    {
        return date;
    }

    public String getUsername()
    {
        return username;
    }

    public GameTime getGameTime()
    {
        return gameTime;
    }

    public Board getBoard()
    {
        return board;
    }

    @Override
    public Long getId()
    {
        return id;
    }

    @Override
    public void setId(final Long id)
    {
        this.id = id;
    }

}
