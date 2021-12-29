package dev.michals3r3k.model.board.components;

/**
 * A class that represents field, which contains bomb.
 */
public class BombField extends Field
{
    public BombField(int rowPosition, int colPosition)
    {
        super(rowPosition, colPosition);
    }

    public BombField(int rowPosition, int colPosition, FieldStatus status)
    {
        super(rowPosition, colPosition, status);
    }

    @Override
    public FieldType getFieldType() {
        return FieldType.BOMB;
    }

}
