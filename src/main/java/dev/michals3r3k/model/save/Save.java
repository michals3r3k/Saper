package dev.michals3r3k.model.save;

import dev.michals3r3k.model.board.Board;

import java.time.LocalDateTime;
import java.util.Objects;

public class Save
{
    private SaveId id;
    private Integer flagQuantity;
    private Board board;
    private GameTime gameTime;
    private LocalDateTime saveTime;

    public Save(Board board)
    {
        this(null, null, board,
            new GameTime(0,0,0), null);
    }

    public Save(
        final SaveId id,
        final Integer flagQuantity,
        final Board board,
        final GameTime time,
        final LocalDateTime saveTime)
    {
        this.id = id;
        this.flagQuantity = flagQuantity;
        this.board = board;
        this.gameTime = time;
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

    public GameTime getGameTime()
    {
        return gameTime;
    }

    public LocalDateTime getSaveTime()
    {
        return saveTime;
    }

    public Integer getFlagQuantity()
    {
        return flagQuantity;
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
