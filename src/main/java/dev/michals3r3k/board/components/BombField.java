package dev.michals3r3k.board.components;

/**
 * A class that represents field, which contains bomb.
 */
public class BombField extends Field
{
    public BombField(int rowPosition, int colPosition)
    {
        super(rowPosition, colPosition);
    }

    @Override
    public FieldType getFieldType() {
        return FieldType.BOMB;
    }

}
