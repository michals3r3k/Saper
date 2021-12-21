package dev.michals3r3k.board.components;

public abstract class Field
{
    private final int rowPosition;
    private final int colPosition;

    public Field(int rowPosition, int colPosition)
    {
        this.rowPosition = rowPosition;
        this.colPosition = colPosition;
    }

    public int getRowPosition()
    {
        return rowPosition;
    }

    public int getColPosition()
    {
        return colPosition;
    }

    public abstract FieldType getFieldType();

}
