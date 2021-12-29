package dev.michals3r3k.board.components;

public abstract class Field
{
    private final int rowPosition;
    private final int colPosition;
    private FieldStatus status;

    public Field(int rowPosition, int colPosition)
    {
        this.rowPosition = rowPosition;
        this.colPosition = colPosition;
        this.status = FieldStatus.COVERED;
    }

    public Field(int rowPosition, int colPosition, FieldStatus status)
    {
        this.rowPosition = rowPosition;
        this.colPosition = colPosition;
        this.status = status;
    }

    public int getRowPosition()
    {
        return rowPosition;
    }

    public int getColPosition()
    {
        return colPosition;
    }

    public FieldStatus getStatus()
    {
        return status;
    }

    public void setStatus(final FieldStatus status)
    {
        this.status = status;
    }

    public abstract FieldType getFieldType();

}
