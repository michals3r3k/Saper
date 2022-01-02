package dev.michals3r3k.frame.game;

import dev.michals3r3k.model.board.Board;
import dev.michals3r3k.model.save.GameTime;

import java.time.LocalDateTime;

public class Score
{
    private LocalDateTime date;
    private String username;
    private GameTime gameTime;
    private Board board;

    public Score(LocalDateTime date, String username, GameTime gameTime, Board board)
    {
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

}
