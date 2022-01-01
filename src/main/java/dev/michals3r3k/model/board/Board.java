package dev.michals3r3k.model.board;

import dev.michals3r3k.model.board.components.Field;

/**
 * A class that represents the saper's game board.
 */
public class Board
{
    private Field[][] fields;
    private final int bombQuantity;
    private boolean calculated;
    private final int width;
    private final int height;

    public Board(Field[][] fields, int bombQuantity)
    {
        this(fields, bombQuantity, false);
    }

    public Board(Field[][] fields, int bombQuantity, boolean calculated)
    {
        this.fields = fields;
        this.bombQuantity = bombQuantity;
        this.height = fields.length;
        this.width = fields[height-1].length;
        this.calculated = calculated;
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

    public void setField(Field field)
    {
        fields[field.getRowPosition()][field.getColPosition()] = field;
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

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

}
