package dev.michals3r3k.factory;

import dev.michals3r3k.model.board.Board;
import dev.michals3r3k.model.board.components.Field;
import dev.michals3r3k.model.board.components.FieldType;
import dev.michals3r3k.model.board.components.RegularField;
import org.junit.Before;
import org.junit.Test;

public class BoardFactoryTest
{
    private static final int ROWS_5 = 5;
    private static final int ROWS_10 = 10;
    private static final int COLS_10 = 10;
    private static final int SATURATION_12 = 12;
    private static final int SATURATION_25 = 25;
    private static final int SATURATION_50 = 50;

    private BoardFactory factory;

    @Before
    public void setUp()
    {
        this.factory = new BoardFactory();
    }

    @Test
    public void testGetBoard10_10_12()
    {
        testGetBoard(ROWS_10, COLS_10, SATURATION_12);
    }

    @Test
    public void testGetBoard10_10_25()
    {
        testGetBoard(ROWS_10, COLS_10, SATURATION_25);
    }

    @Test
    public void testGetBoard10_10_50()
    {
        testGetBoard(ROWS_10, COLS_10, SATURATION_50);
    }

    @Test
    public void testGetBoard5_10_50()
    {
        testGetBoard(ROWS_5, COLS_10, SATURATION_50);
    }

    private void testGetBoard(int noRows, int noCols, int saturation)
    {
        Board board = factory.getBoard(noRows, noCols, saturation);
        Field[][] rows = board.getFields();
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for(Field[] row : rows)
        {
            sb.append("\t[");
            for(Field field : row)
            {
                if(field.getFieldType() == FieldType.REGULAR)
                {
                    sb.append(((RegularField) field).getValue()).append(", ");
                }
                else if(field.getFieldType() == FieldType.BOMB)
                {
                    sb.append("X, ");
                }
                else
                {
                    sb.append("-, ");
                }
            }
            sb.append("]\n");
        }
        sb.append("]");
        System.out.println(sb);
    }

}
