package dev.michals3r3k.board.components;

/**
 * A class that represents the regular field with information
 * about quantity of bombs in this field's neighbourhood.
 */
public class RegularField extends Field
{
    private int value;

    public RegularField(int rowPosition, int colPosition, int value)
    {
        super(rowPosition, colPosition);
        this.value = value;
    }

    public RegularField(int rowPosition, int colPosition, FieldStatus status, int value)
    {
        super(rowPosition, colPosition, status);
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    @Override
    public FieldType getFieldType()
    {
        return FieldType.REGULAR;
    }

}
