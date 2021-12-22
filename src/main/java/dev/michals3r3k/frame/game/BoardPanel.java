package dev.michals3r3k.frame.game;

import dev.michals3r3k.board.Board;
import dev.michals3r3k.board.components.BombField;
import dev.michals3r3k.board.components.FieldType;
import dev.michals3r3k.board.components.RegularField;
import dev.michals3r3k.factory.BoardFactory;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel
{
    private Board board;
    private FieldPanel[][] fieldPanels;

    BoardPanel(int cols, int rows, int saturation)
    {
        this.board = new BoardFactory().getBoard(rows, cols, saturation);
        this.fieldPanels = new FieldPanel[rows][cols];
        this.setLayout(new GridLayout(rows, cols));
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < cols; j++)
            {
                FieldPanel fieldPanel = new FieldPanel(i, j);
                if(board.getFields()[i][j] instanceof BombField)
                {
                    fieldPanel.setValue("B");
                }
                fieldPanels[i][j] = fieldPanel;
                this.add(fieldPanel);
            }
        }
    }

    void updateFields()
    {
        int rows = board.getFields().length;
        int cols = board.getFields()[board.getFields().length - 1].length;
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < cols; j++)
            {
                if(board.getFields()[i][j].getFieldType() == FieldType.REGULAR)
                {
                    RegularField field = (RegularField) board.getFields()[i][j];
                    int value = field.getValue();
                    if(value > 0)
                    {
                        fieldPanels[i][j].setValue(value);
                    }
                } else if(board.getFields()[i][j].getFieldType() == FieldType.BOMB)
                {
                    fieldPanels[i][j].setValue("B");
                }
            }
        }
    }

    Board getBoard()
    {
        return board;
    }

    FieldPanel getFieldPanel(int x, int y)
    {
        return fieldPanels[x][y];
    }

}
