package dev.michals3r3k.user;

import dev.michals3r3k.board.Board;

import java.time.LocalDateTime;

public class Save
{
    private SaveId id;
    private Board board;
    private int minutes;
    private int seconds;
    private LocalDateTime saveTime;

    public Save(
        final SaveId id,
        final Board board,
        final int minutes,
        final int seconds,
        final LocalDateTime saveTime)
    {
        this.id = id;
        this.board = board;
        this.minutes = minutes;
        this.seconds = seconds;
        this.saveTime = saveTime;
    }

    public SaveId getId()
    {
        if(id == null || id.getSaveId() == null || id.getUsername() == null)
        {
            return null;
        }
        return id;
    }

    public void setId(final SaveId id)
    {
        this.id = id;
    }

    public Board getBoard()
    {
        return board;
    }

    public int getMinutes()
    {
        return minutes;
    }

    public int getSeconds()
    {
        return seconds;
    }

    public LocalDateTime getSaveTime()
    {
        return saveTime;
    }

}
