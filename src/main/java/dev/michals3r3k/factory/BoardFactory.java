package dev.michals3r3k.factory;

import dev.michals3r3k.model.board.Board;
import dev.michals3r3k.model.board.components.*;
import dev.michals3r3k.frame.game.GameFrame;

import java.util.Random;

/**
 * Factory that creates the saper's game board.
 */
public class BoardFactory
{
    int bombQuantity;

    public BoardFactory()
    {
        this.bombQuantity = 0;
    }

    public void swapBomb(int x, int y, Board board)
    {
        Field[][] fields = board.getFields();
        if(fields[x][y].getFieldType() != FieldType.BOMB)
        {
            throw new IllegalStateException("It's not a bomb");
        }
        EmptyField emptyFound = null;
        for(int i = 0; i < fields.length; i++)
        {
            for(int j = 0; j < fields[i].length; j++)
            {
                Field field = fields[i][j];
                if(field.getFieldType() == FieldType.EMPTY)
                {
                    emptyFound = (EmptyField) field;
                    break;
                }
            }
            if(emptyFound != null)
            {
                break;
            }
        }
        if(emptyFound == null)
        {
            throw new IllegalStateException("Board creation error");
        }
        int xFound = emptyFound.getRowPosition();
        int yFound = emptyFound.getColPosition();
        fields[xFound][yFound] = new BombField(xFound, yFound, FieldStatus.COVERED);
        fields[x][y] = new EmptyField(x, y, FieldStatus.UNCOVERED);
        board.setFields(fields);
    }

    public Board getBoard(int rows, int cols, int saturation)
    {
        return new Board(getBombBoardFields(rows, cols, saturation), bombQuantity);
    }

    private Field[][] getBombBoardFields(int rows, int cols, int bombSaturation)
    {
        final Field[][] emptyFields = getEmptyBoardFields(rows, cols);
        for(int row = 0; row < emptyFields.length; row++)
        {
            Field[] fields = emptyFields[row];
            for(int col = 0; col < fields.length; col++)
            {
                if(isShouldPutBomb(bombSaturation))
                {
                    fields[col] = new BombField(row, col);
                    ++bombQuantity;
                }
            }
            emptyFields[row] = fields;
        }
        return emptyFields;
    }

    private Field[][] getEmptyBoardFields(int rows, int cols)
    {
        Field[][] fields = new Field[rows][];
        for(int row = 0; row < rows; row++)
        {
            fields[row] = getEmptyFields(row, cols);
        }
        return fields;
    }

    private Field[] getEmptyFields(int row, int cols)
    {
        Field[] emptyFields = new Field[cols];
        for(int col = 0; col < cols; col++)
        {
            emptyFields[col] = new EmptyField(row, col);
        }
        return emptyFields;
    }

    private boolean isShouldPutBomb(int bombSaturation)
    {
        Random random = new Random();
        return random.nextInt(100) < bombSaturation;
    }

    public void calculateRegularFields(Board board, GameFrame gameFrame)
    {
        calculateRegularFields(board);
        gameFrame.setFlagQuantity(board.getBombQuantity());
    }

    public void calculateRegularFields(Board board)
    {
        Field[][] bombFields = board.getFields();
        for(int row = 0; row < bombFields.length; row++)
        {
            for(int col = 0; col < bombFields[row].length; col++)
            {
                bombFields[row][col] = getRegularField(row, col, bombFields);
            }
        }
        board.setFields(bombFields);
        board.setCalculated(true);
    }

    private Field getRegularField(int row, int col, Field[][] bombFields)
    {
        Field currentField = bombFields[row][col];
        if(isBomb(currentField))
        {
            return currentField;
        }
        int bombCount = 0;
        if(row - 1 >= 0 && col - 1 >= 0 && isBomb(bombFields[row - 1][col - 1])) //left-top
        {
            ++bombCount;
        }
        if(row - 1 >= 0 && isBomb(bombFields[row - 1][col])) //top
        {
            ++bombCount;
        }
        int colSize = bombFields[row].length;
        if(row - 1 >= 0 && col + 1 < colSize && isBomb(bombFields[row - 1][col + 1])) //top-right
        {
            ++bombCount;
        }
        if(col - 1 >= 0 && isBomb(bombFields[row][col - 1])) //left
        {
            ++bombCount;
        }
        if(col + 1 < colSize && isBomb(bombFields[row][col + 1])) //right
        {
            ++bombCount;
        }
        int rowSize = bombFields.length;
        if(row + 1 < rowSize && col - 1 >= 0 && isBomb(bombFields[row + 1][col - 1])) //left-bottom
        {
            ++bombCount;
        }
        if(row + 1 < rowSize && isBomb(bombFields[row + 1][col])) //bottom
        {
            ++bombCount;
        }
        if(row + 1 < rowSize && col + 1 < colSize && isBomb(bombFields[row + 1][col + 1])) //bottom right
        {
            ++bombCount;
        }
        if(bombCount == 0)
        {
            return currentField;
        }
        return new RegularField(row, col, bombCount);
    }

    private boolean isBomb(Field field)
    {
        return field.getFieldType() == FieldType.BOMB;
    }

}
