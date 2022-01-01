package dev.michals3r3k.frame.game;

import dev.michals3r3k.context.Context;
import dev.michals3r3k.context.SaveContext;
import dev.michals3r3k.context.UserContext;
import dev.michals3r3k.dao.SaveREPO;
import dev.michals3r3k.dao.Saveable;
import dev.michals3r3k.frame.menu.GameParams;
import dev.michals3r3k.frame.menu.MenuFrame;
import dev.michals3r3k.model.board.Board;
import dev.michals3r3k.model.save.GameTime;
import dev.michals3r3k.model.save.Save;
import dev.michals3r3k.model.save.SaveId;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class GameFrame extends JFrame
{
    private final Board board;
    private JLabel flagLabel;
    private Integer flagQuantity;
    private GameTimer gameTimer;

    public GameFrame(Save save)
    {
        this.board = save.getBoard();
        JPanel headerPanel = getHeader(save.getFlagQuantity(),
            save.getGameTime());
        TilePanel tilePanel = new TilePanel(board, this);
        int boardWidth = board.getWidth() * GameParams.TILE_SIZE;
        int boardHeight = board.getHeight() * GameParams.TILE_SIZE;
        tilePanel.setBounds(0, 0, boardWidth, boardHeight);

        JLayeredPane boardContent = new JLayeredPane();
        boardContent.setBounds(100, 150, boardWidth, boardHeight);
        boardContent.add(tilePanel, JLayeredPane.DEFAULT_LAYER);

        int frameWidth = getFrameWidthAndCenterComponents(headerPanel, boardContent);
        int gamePanelHeight = boardHeight + 200;
        this.setTitle(GameParams.APP_TITLE);
        this.setSize(frameWidth, gamePanelHeight);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.add(headerPanel);
        this.add(boardContent);
        this.setVisible(true);
    }

    private int getFrameWidthAndCenterComponents(
        final JPanel headerPanel,
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

    private JPanel getHeader(Integer flagQuantity, GameTime gameTime)
    {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(null);
        this.flagQuantity = flagQuantity;
        this.flagLabel = new JLabel();
        this.gameTimer = new GameTimer(gameTime);
        if(board.isCalculated())
        {
            gameTimer.start();
        }
        flagLabel.setBounds(140, 0, 40, 20);
        if(flagQuantity != null)
        {
            flagLabel.setText(flagQuantity.toString());
        }
        else
        {
            flagLabel.setText("");
        }
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
        saveButton.addActionListener(saveGame());

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
            Context context = Context.getContext();
            SaveContext saveContext = SaveContext.getSaveContext(context);
            saveContext.setSave(null);
            new MenuFrame();
        };
    }

    private ActionListener saveGame()
    {
        return (e) -> {
            Context context = Context.getContext();
            SaveContext saveContext = SaveContext.getSaveContext(context);
            Saveable saveable = new SaveREPO();
            Save save = getSave(saveContext, board);
            saveable.saveOrUpdate(save);
            saveContext.setSave(save);
        };
    }

    private Save getSave(final SaveContext saveContext, Board board)
    {
        SaveId saveIdOrNull = saveContext.getSaveIdOrNull();
        return new Save(saveIdOrNull, flagQuantity, board,
            gameTimer.getGameTime(), LocalDateTime.now());
    }

    public Integer getFlagQuantity()
    {
        return flagQuantity;
    }

    public void setFlagQuantity(Integer flagQuantity)
    {
        this.flagQuantity = flagQuantity;
        getFlagLabel().setText(flagQuantity.toString());
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
        gameTimer.stop();
        JFrame winFrame = new JFrame();
        winFrame.setTitle("You win!");
        winFrame.setVisible(true);
        winFrame.setSize(500, 300);
        winFrame.setLayout(null);
        Context context = Context.getContext();
        UserContext userContext = UserContext.getUserContext(context);
        JLabel congratsLabel = new JLabel("CONGRATULATIONS " +
            userContext.getCurrentUserName() + " YOU WIN!");
        congratsLabel.setBounds(90, 50, 400, 40);
        JLabel messageLabel = new JLabel("You want to save your score?");
        messageLabel.setBounds(90, 100, 400, 40);
        JButton backToMenuButton = new JButton("No");
        backToMenuButton.addActionListener(e-> {
            winFrame.dispose();
            this.dispose();
            new MenuFrame();
        });
        backToMenuButton.setBounds(90, 150, 90, 40);
        JButton saveScoreButton = new JButton("Yes");
        saveScoreButton.addActionListener(e-> {

        });
        saveScoreButton.setBounds(250, 150, 90, 40);
        winFrame.add(congratsLabel);
        winFrame.add(messageLabel);
        winFrame.add(backToMenuButton);
        winFrame.add(saveScoreButton);
    }

    public void gameLost()
    {
        gameTimer.stop();
        JFrame loseFrame = new JFrame();
        loseFrame.setTitle("You lost!");
        loseFrame.setVisible(true);
        loseFrame.setSize(400, 200);
        loseFrame.setLayout(null);
        JLabel jLabel = new JLabel("OH NO! YOU EXPLODED!");
        jLabel.setBounds(115, 50, 200, 50);
        JButton backToMenuButton = new JButton("Back to Menu");
        backToMenuButton.addActionListener(e-> {
            loseFrame.dispose();
            this.dispose();
            new MenuFrame();
        });
        backToMenuButton.setBounds(100, 110, 200, 40);
        loseFrame.add(jLabel);
        loseFrame.add(backToMenuButton);
    }

}
