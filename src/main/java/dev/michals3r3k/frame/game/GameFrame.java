package dev.michals3r3k.frame.game;

import dev.michals3r3k.board.Board;
import dev.michals3r3k.factory.BoardFactory;
import dev.michals3r3k.frame.menu.GameParams;
import dev.michals3r3k.user.Save;

import javax.swing.*;

public class GameFrame extends JFrame
{
    final JLabel flagLabel;
    Integer flagQuantity;
    final GameTimer gameTimer;

    public GameFrame(int cols, int rows, int saturation)
    {
        this.flagQuantity = null;
        this.flagLabel = new JLabel();

        this.gameTimer = new GameTimer();
        gameTimer.setBounds(260, 20, 160, 100);

        int boardWidth = cols * GameParams.TILE_SIZE;
        int boardHeight = rows * GameParams.TILE_SIZE;

        flagLabel.setText("");
        flagLabel.setBounds(140, 0, 40, 20);

        JLabel label = new JLabel("Flags remaining:");
        label.setBounds(0, 0, 130, 20);

        JPanel flagPanel = new JPanel();
        flagPanel.setLayout(null);
        flagPanel.setBounds(50, 50, 180, 20);
        flagPanel.add(flagLabel);
        flagPanel.add(label);

        JLayeredPane boardContent = new JLayeredPane();
        boardContent.setBounds(100, 150, boardWidth, boardHeight);

        Board board = new BoardFactory().getBoard(rows, cols, saturation);
        TilePanel tilePanel = new TilePanel(board, this);
        tilePanel.setBounds(0, 0, boardWidth, boardHeight);

        boardContent.add(tilePanel, JLayeredPane.DEFAULT_LAYER);
        Save save = new Save(null, board, gameTimer.getMinutes(), gameTimer.getSeconds());
        save.save();

        this.setTitle(GameParams.APP_TITLE);
        this.setSize(cols * GameParams.TILE_SIZE + 200, rows * GameParams.TILE_SIZE + 200);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.add(flagPanel);
        this.add(boardContent);
        this.add(gameTimer);
        this.setVisible(true);
    }

    public Integer getFlagQuantity()
    {
        return flagQuantity;
    }

    public void setFlagQuantity(Integer flagQuantity)
    {
        getFlagLabel().setText(flagQuantity.toString());
        this.flagQuantity = flagQuantity;
    }

    public JLabel getFlagLabel()
    {
        return flagLabel;
    }

    public void addFlag()
    {
        Integer flagQuantity = getFlagQuantity();
        if(flagQuantity != null)
        {
            setFlagQuantity(flagQuantity + 1);
            return;
        }
        throw new IllegalStateException("FlagQuantity is null!");
    }

    public void subtractFlag()
    {
        Integer flagQuantity = getFlagQuantity();
        if(flagQuantity != null)
        {
            setFlagQuantity(flagQuantity - 1);
            return;
        }
        throw new IllegalStateException("FlagQuantity is null!");
    }

    public GameTimer getGameTimer()
    {
        return gameTimer;
    }

    public boolean isCanPutFlag()
    {
        return getFlagQuantity() != null && getFlagQuantity() > 0;
    }

    public void gameWin()
    {
        getGameTimer().getTimer().stop();
        JFrame jFrame = new JFrame();
        jFrame.setVisible(true);
        jFrame.setSize(300, 200);
        JLabel jLabel = new JLabel("YOU WIN!");
        jFrame.add(jLabel);
    }

}
