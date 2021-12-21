package dev.michals3r3k.board.components;

/**
 * A class that represents empty field.
 */
public class EmptyField extends Field
{
    public EmptyField(int rowPosition, int colPosition)
    {
        super(rowPosition, colPosition);
    }

    @Override
    public FieldType getFieldType()
    {
        return FieldType.EMPTY;
    }

}
