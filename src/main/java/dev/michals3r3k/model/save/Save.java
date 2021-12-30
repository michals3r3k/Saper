package dev.michals3r3k.model.save;

import dev.michals3r3k.model.board.Board;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        final Save save = (Save) o;
        return Objects.equals(id, save.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(id);
    }

}
