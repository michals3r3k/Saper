package dev.michals3r3k.board;

import dev.michals3r3k.board.components.Field;

/**
 * A class that represents the saper's game board.
 */
public class Board
{
    private Field[][] fields;
    private final int bombQuantity;
    private boolean calculated;

    public Board(Field[][] fields, int bombQuantity)
    {
        this.fields = fields;
        this.bombQuantity = bombQuantity;
    }

    public Field[][] getFields()
    {
        return fields;
    }

    public void setFields(Field[][] fields)
    {
        this.fields = fields;
    }

    public Field getField(int x, int y)
    {
        return fields[x][y];
    }

    public int getBombQuantity()
    {
        return bombQuantity;
    }

    public boolean isCalculated()
    {
        return calculated;
    }

    public void setCalculated(boolean calculated)
    {
        this.calculated = calculated;
    }

}
