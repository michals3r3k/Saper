package dev.michals3r3k.frame.game;

import dev.michals3r3k.board.Board;
import dev.michals3r3k.board.components.BombField;
import dev.michals3r3k.board.components.EmptyField;
import dev.michals3r3k.board.components.Field;
import dev.michals3r3k.board.components.FieldType;
import dev.michals3r3k.factory.BoardFactory;
import dev.michals3r3k.frame.menu.GameParams;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Tile extends JPanel implements MouseListener {
    int x;
    int y;
    private GameFrame gameFrame;
    boolean flag;
    BoardPanel boardPanel;
    BoardFactory boardFactory;
    private TilePanel tilePanel;
    private JLayeredPane boardContainer;

    public Tile(int x, int y, BoardPanel boardPanel, TilePanel tilePanel, JLayeredPane boardContainer, GameFrame gameFrame)
    {
        this.x = x;
        this.y = y;
        this.gameFrame = gameFrame;
        this.flag = false;
        this.tilePanel = tilePanel;
        this.boardContainer = boardContainer;
        this.boardFactory = new BoardFactory();
        this.boardPanel = boardPanel;
        this.setBackground(Color.LIGHT_GRAY);

        this.setSize(new Dimension(GameParams.TILE_SIZE, GameParams.TILE_SIZE));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        this.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(isLeftMouseButton(e) && !isFlag() && tilePanel.isCanPlay())
        {
            leftClickAction();
            return;
        }
        if(isRightMouseButton(e) && tilePanel.isCanPlay() && boardPanel.getBoard().isCalculated())
        {
            toggleFlag();
        }
    }

    private void toggleFlag() {
        if(!isFlag() && !gameFrame.isCanPutFlag())
        {
            return;
        }
        setFlag(!isFlag());
        if(isFlag() && gameFrame.isCanPutFlag())
        {
            gameFrame.subtractFlag();
            this.setBackground(new Color(40, 153, 118));
            return;
        }
        gameFrame.addFlag();
        this.setBackground(Color.LIGHT_GRAY);
    }

    private void leftClickAction() {
        Board board = boardPanel.getBoard();
        Field field = board.getField(x, y);
        this.setVisible(false);
        if(tilePanel.isAllNonBombsUncovered())
        {
            gameFrame.gameWin();
        }
        if(field instanceof BombField && !board.isCalculated())
        {
            boardFactory.swapBomb(x, y, board);
            boardFactory.calculateRegularFields(board, gameFrame);
            boardPanel.updateFields();
            gameFrame.getGameTimer().getTimer().start();
            return;
        }
        if(!board.isCalculated())
        {
            boardFactory.calculateRegularFields(board, gameFrame);
            boardPanel.updateFields();
            if (boardPanel.getBoard().getField(x, y) instanceof EmptyField)
            {
                tilePanel.uncoverEmptyNeighbours(x, y);
            }
            gameFrame.getGameTimer().getTimer().start();
            return;
        }
        if(field.getFieldType() == FieldType.BOMB)
        {
            boardPanel.getFieldPanel(x ,y).setBackground(Color.red);
            tilePanel.uncoverBombs();
            tilePanel.setCanPlay(false);
            gameFrame.getGameTimer().getTimer().stop();
        }
        if(field instanceof EmptyField)
        {
            tilePanel.uncoverEmptyNeighbours(x, y);
        }
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getPosX() {
        return x;
    }

    public int getPosY() {
        return y;
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return x == tile.x && y == tile.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
