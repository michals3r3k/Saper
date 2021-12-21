package dev.michals3r3k.frame.game;

import dev.michals3r3k.board.Board;
import dev.michals3r3k.board.components.BombField;
import dev.michals3r3k.board.components.RegularField;
import dev.michals3r3k.factory.BoardFactory;
import dev.michals3r3k.frame.menu.GameParams;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel{
    Board board;
    JPanel[][] boardPanels;
    FieldPanel[][] fieldPanels;


    BoardPanel(int cols, int rows, int saturation)
    {
        this.board = new BoardFactory().getBoard(rows,cols,saturation);
        this.fieldPanels = new FieldPanel[rows][cols];
        this.setLayout(new GridLayout(rows, cols));
        for(int i = 0; i< rows; i++)
        {
            for(int j = 0; j< cols; j++)
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
//        fill();
    }

    void updateFields()
    {
        int rows = board.getFields().length;
        int cols = board.getFields()[board.getFields().length-1].length;
        for(int i = 0; i< rows; i++)
        {
            for(int j = 0; j< cols; j++)
            {
                if(board.getFields()[i][j] instanceof RegularField)
                {
                    RegularField field = (RegularField) board.getFields()[i][j];
                    int value = field.getValue();
                    if(value>0)
                    {
                        fieldPanels[i][j].setValue(value);
                    }
                }
                else if(board.getFields()[i][j] instanceof BombField)
                {
                    fieldPanels[i][j].setValue("B");
                }
            }
        }
    }

    void fill() {
        this.removeAll();
        int rows = board.getFields().length;
        int cols = board.getFields()[board.getFields().length-1].length;
        for(int i = 0; i< rows; i++)
        {
            for(int j = 0; j< cols; j++)
            {
                JPanel jPanel = new JPanel();
                jPanel.setSize(new Dimension(GameParams.TILE_SIZE, GameParams.TILE_SIZE));
                jPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                jPanel.setLayout(new BorderLayout());
                jPanel.setBackground(Color.DARK_GRAY);

                if(board.getFields()[i][j] instanceof RegularField)
                {
                    RegularField field = (RegularField) board.getFields()[i][j];
                    int value = field.getValue();
                    if(value>0)
                    {
                        jPanel.setLayout(new BorderLayout());
                        JLabel jLabel = new JLabel("" + value);
                        jLabel.setForeground(GameParams.TILE_VALUE_COLOR.get(value));
                        jLabel.setVerticalAlignment(JLabel.CENTER);
                        jLabel.setHorizontalAlignment(JLabel.CENTER);
                        jPanel.add(jLabel);
                    }
                }
                else if(board.getFields()[i][j] instanceof BombField)
                {
                    jPanel.setLayout(new BorderLayout());
                    JLabel jLabel = new JLabel("B");
                    jLabel.setForeground(Color.BLACK);
                    jLabel.setVerticalAlignment(JLabel.CENTER);
                    jLabel.setHorizontalAlignment(JLabel.CENTER);
                    jPanel.add(jLabel);
                }
                boardPanels[i][j] = jPanel;
                this.add(jPanel);
            }
        }
        this.revalidate();
        this.repaint();
    }

    Board getBoard()
    {
        return board;
    }

    FieldPanel[][] getFieldPanels()
    {
        return fieldPanels;
    }

    FieldPanel getFieldPanel(int x, int y)
    {
        return fieldPanels[x][y];
    }


}
