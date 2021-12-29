package dev.michals3r3k.model.board.components;

/**
 * A class that represents empty field.
 */
public class EmptyField extends Field
{
    public EmptyField(int rowPosition, int colPosition)
    {
        super(rowPosition, colPosition);
    }

    public EmptyField(int rowPosition, int colPosition, FieldStatus status)
    {
        super(rowPosition, colPosition, status);
    }

    @Override
    public FieldType getFieldType()
    {
        return FieldType.EMPTY;
    }

}
