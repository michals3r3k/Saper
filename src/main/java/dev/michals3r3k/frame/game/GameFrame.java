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

        Board board = boardFactory.getBoard(rows, cols, saturation);
        TilePanel tilePanel = new TilePanel(board, this);
        tilePanel.setBounds(0, 0, boardWidth, boardHeight);

        JLayeredPane boardContent = new JLayeredPane();
        boardContent.setBounds(100, 150, boardWidth, boardHeight);
        boardContent.add(tilePanel, JLayeredPane.DEFAULT_LAYER);

        JButton saveButton = new JButton("Save Game");
        saveButton.setBounds(gameTimer.getX() + gameTimer.getWidth() + 50,
            gameTimer.getY(), 130, 40);
        saveButton.addActionListener(saveGame(board));

        JButton menuButton = new JButton("Main menu");
        menuButton.setBounds(saveButton.getX(),
            saveButton.getY() + saveButton.getHeight() + 10, 130, 40);
        menuButton.addActionListener(mainMenu());


        int rightEdge = saveButton.getWidth() + saveButton.getX() + 50;
        int gamePanelAreaWidth = cols * GameParams.TILE_SIZE + 200;
        int gamePanelAreaHeight = rows * GameParams.TILE_SIZE + 200;

        this.setTitle(GameParams.APP_TITLE);
        this.setSize(Math.max(gamePanelAreaWidth, rightEdge), gamePanelAreaHeight);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.add(flagPanel);
        this.add(boardContent);
        this.add(gameTimer);
        this.add(saveButton);
        this.add(menuButton);
        this.setVisible(true);
    }

    private ActionListener mainMenu()
    {
        return (e) ->{
            this.dispose();
            new MenuFrame();
        };
    }

    private ActionListener saveGame(Board board)
    {
        return (e) ->{
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
