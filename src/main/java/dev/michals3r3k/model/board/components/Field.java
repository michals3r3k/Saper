package dev.michals3r3k.model.board.components;

public abstract class Field
{
    private  int rowPosition;
    private  int colPosition;
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

    public void setRowPosition(final int rowPosition)
    {
        this.rowPosition = rowPosition;
    }

    public int getColPosition()
    {
        return colPosition;
    }

    public void setColPosition(final int colPosition)
    {
        this.colPosition = colPosition;
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
