package dev.michals3r3k.frame.game;

import dev.michals3r3k.model.board.Board;
import dev.michals3r3k.factory.BoardFactory;
import dev.michals3r3k.frame.menu.GameParams;
import dev.michals3r3k.model.board.components.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Tile extends JPanel implements MouseListener
{
    private final Board board;
    private final Position position;
    private final GameFrame gameFrame;
    private final BoardFactory boardFactory;
    private final TilePanel tilePanel;
    private final JLabel label;

    public Tile(
        Board board,
        Position position,
        TilePanel tilePanel,
        GameFrame gameFrame)
    {
        this.board = board;
        this.position = position;
        this.gameFrame = gameFrame;
        this.tilePanel = tilePanel;
        this.boardFactory = new BoardFactory();
        this.label = new JLabel();
        this.setTextColor(Color.BLACK);
        this.setBackground();
        this.setSize(new Dimension(GameParams.TILE_SIZE, GameParams.TILE_SIZE));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        this.addMouseListener(this);
        this.add(label);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if(!tilePanel.isCanPlay())
        {
            return;
        }
        if(isLeftMouseButton(e) && !isFlag())
        {
            leftClickAction();
            return;
        }
        if(isRightMouseButton(e) && board.isCalculated())
        {
            toggleFlag();
        }
    }

    private void toggleFlag()
    {
        if(!isFlag() && !gameFrame.isCanPutFlag() || getStatus() == FieldStatus.UNCOVERED)
        {
            return;
        }
        if(isFlag())
        {
            setStatus(FieldStatus.COVERED);
        }
        else if(getStatus() == FieldStatus.COVERED)
        {
            setStatus(FieldStatus.FLAG);
        }
        if(isFlag() && gameFrame.isCanPutFlag())
        {
            gameFrame.subtractFlag();
            this.setBackground(GameParams.FLAG_COLOR);
            return;
        }
        gameFrame.addFlag();
        this.setBackground(Color.LIGHT_GRAY);
    }

    private void leftClickAction()
    {
        Timer timer = gameFrame.getGameTimer().getTimer();
        if(!timer.isRunning())
        {
            timer.start();
        }
        setStatus(FieldStatus.UNCOVERED);
        this.setBackground(Color.DARK_GRAY);
        if(tilePanel.isAllNonBombsUncovered())
        {
            gameFrame.gameWin();
            return;
        }
        if(!board.isCalculated())
        {
            calculateBoard();
        }
        Field field = getField();
        if(field.getFieldType() == FieldType.EMPTY)
        {
            tilePanel.uncoverEmptyNeighbours(getPosX(), getPosY());
            return;
        }
        if(field.getFieldType() == FieldType.BOMB)
        {
            bombClickAction();
            return;
        }
        RegularField regularField = (RegularField) field;
        setValue(regularField.getValue());
    }

    private void calculateBoard()
    {
        Field field = getField();
        if(field.getFieldType() == FieldType.BOMB)
        {
            boardFactory.swapBomb(getPosX(), getPosY(), board);
        }
        boardFactory.calculateRegularFields(board, gameFrame);
    }

    private void bombClickAction()
    {
        this.setBackground(Color.RED);
        tilePanel.uncoverBombs(position);
        tilePanel.setCanPlay(false);
        gameFrame.getGameTimer().getTimer().stop();
    }

    public void setValue(Integer value)
    {
        setTextColor(GameParams.TILE_VALUE_COLOR.get(value));
        setText(value.toString());
    }

    public void setText(String text)
    {
        label.setText(text);
    }

    public void setTextColor(Color color)
    {
        label.setForeground(color);
    }

    public boolean isFlag()
    {
        return getStatus() == FieldStatus.FLAG;
    }

    public void setStatus(FieldStatus fieldStatus)
    {
        getField().setStatus(fieldStatus);
    }

    private FieldStatus getStatus()
    {
        return getField().getStatus();
    }

    public Field getField()
    {
        return board.getField(position.getX(), position.getY());
    }

    public int getPosX()
    {
        return position.getX();
    }

    public int getPosY()
    {
        return position.getY();
    }

    public FieldType getFieldType()
    {
        return getField().getFieldType();
    }

    public void setBackground()
    {
        switch(getStatus())
        {
            case COVERED -> {
                this.setBackground(Color.LIGHT_GRAY);
            }
            case FLAG -> {
                this.setBackground(GameParams.FLAG_COLOR);
            }
            case UNCOVERED -> {
                this.setBackground(Color.DARK_GRAY);
                this.setAppearance();
            }
        }

    }

    private void setAppearance()
    {
        switch(getFieldType())
        {
            case REGULAR -> {
                RegularField field = (RegularField) getField();
                setValue(field.getValue());
            }
            case BOMB -> {
                setText("B");
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        final Tile tile = (Tile) o;
        return position.equals(tile.position);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(position);
    }

}
