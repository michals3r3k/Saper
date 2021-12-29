package dev.michals3r3k.frame.game;

import dev.michals3r3k.model.board.Board;
import dev.michals3r3k.factory.BoardFactory;
import dev.michals3r3k.dao.SaveREPO;
import dev.michals3r3k.frame.menu.GameParams;
import dev.michals3r3k.frame.menu.MenuFrame;
import dev.michals3r3k.model.save.Save;
import dev.michals3r3k.model.save.SaveId;
import dev.michals3r3k.dao.Saveable;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class GameFrame extends JFrame
{
    private final BoardFactory boardFactory = new BoardFactory();
    private final JLabel flagLabel;
    private Integer flagQuantity;
    private final GameTimer gameTimer;
    private SaveId saveId;

    public GameFrame(int cols, int rows, int saturation)
    {
        this.flagQuantity = null;
        this.flagLabel = new JLabel();
        this.gameTimer = new GameTimer();
        Board board = boardFactory.getBoard(rows, cols, saturation);
        JPanel headerPanel = getHeader(board);

        TilePanel tilePanel = new TilePanel(board, this);
        int boardWidth = cols * GameParams.TILE_SIZE;
        int boardHeight = rows * GameParams.TILE_SIZE;
        tilePanel.setBounds(0, 0, boardWidth, boardHeight);

        JLayeredPane boardContent = new JLayeredPane();
        boardContent.setBounds(100, 150, boardWidth, boardHeight);
        boardContent.add(tilePanel, JLayeredPane.DEFAULT_LAYER);

        int frameWidth = centrifyAndGetFrameWidth(headerPanel, boardContent);
        int gamePanelHeight = boardHeight + 200;

        this.setTitle(GameParams.APP_TITLE);
        this.setSize(frameWidth, gamePanelHeight);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.add(headerPanel);
        this.add(boardContent);
        this.setVisible(true);
    }

    private int centrifyAndGetFrameWidth(final JPanel headerPanel,
                                         final JLayeredPane boardContent)
    {
        int headerWidth = headerPanel.getWidth() + (2 * headerPanel.getX());
        int gamePanelWidth = boardContent.getWidth() + (2 * boardContent.getX());
        int frameWidth = Math.max(gamePanelWidth, headerWidth);
        if(frameWidth != headerWidth)
        {
            int x = frameWidth - headerWidth;
            headerPanel.setBounds(x / 2, headerPanel.getY(),
                headerPanel.getWidth(), headerPanel.getHeight());
        } else
        {
            int x = frameWidth - gamePanelWidth;
            boardContent.setBounds(100 + (x / 2), boardContent.getY(),
                boardContent.getWidth(), boardContent.getHeight());
        }
        return frameWidth;
    }

    private JPanel getHeader(Board board)
    {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(null);
        flagLabel.setText("");
        flagLabel.setBounds(140, 0, 40, 20);

        JLabel label = new JLabel("Flags remaining:");
        label.setBounds(0, 0, 130, 20);

        JPanel flagPanel = new JPanel();
        flagPanel.setLayout(null);
        flagPanel.setBounds(0, 30, 180, 20);
        flagPanel.add(flagLabel);
        flagPanel.add(label);

        gameTimer.setBounds(flagPanel.getX() + flagPanel.getWidth() + 30,
            0, 160, 100);

        JButton saveButton = new JButton("Save Game");
        saveButton.setBounds(gameTimer.getX() + gameTimer.getWidth() + 50,
            0, 130, 40);
        saveButton.addActionListener(saveGame(board));

        JButton menuButton = new JButton("Main menu");
        menuButton.setBounds(saveButton.getX(),
            saveButton.getY() + saveButton.getHeight() + 10, 130, 40);
        menuButton.addActionListener(mainMenu());

        headerPanel.add(flagPanel);
        headerPanel.add(gameTimer);
        headerPanel.add(saveButton);
        headerPanel.add(menuButton);
        headerPanel.setBounds(20, 20,
            saveButton.getX() + saveButton.getWidth() + 10,
            menuButton.getY() + menuButton.getHeight() + 10);
        return headerPanel;
    }

    private ActionListener mainMenu()
    {
        return (e) -> {
            this.dispose();
            new MenuFrame();
        };
    }

    private ActionListener saveGame(Board board)
    {
        return (e) -> {
            Save save = new Save(saveId, board, gameTimer.getMinutes(),
                gameTimer.getSeconds(), LocalDateTime.now());
            Saveable saveable = new SaveREPO();
            saveable.save(save);
            this.saveId = save.getId();
            System.out.println(saveId.getSaveId() + " | " + saveId.getUsername());
        };
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
