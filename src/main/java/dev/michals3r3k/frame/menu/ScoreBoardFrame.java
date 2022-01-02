package dev.michals3r3k.frame.menu;

import dev.michals3r3k.GameParams;
import dev.michals3r3k.dao.score.ScoreDAO;
import dev.michals3r3k.frame.menu.main.MenuFrame;
import dev.michals3r3k.model.save.GameTime;
import dev.michals3r3k.model.score.Score;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreBoardFrame extends JFrame
{
    private final static DateTimeFormatter DTF =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final ScoreDAO scoreDAO = new ScoreDAO();

    public ScoreBoardFrame()
    {
        JLabel headerLabel = new JLabel("SAPER BY MICHAL S");
        headerLabel.setBounds(20, 0, 500, 60);
        headerLabel.setFont(new Font(null, Font.BOLD, 30));
        headerLabel.setForeground(Color.DARK_GRAY);
        JPanel headerPanel = new JPanel(null);
        headerPanel.setBounds(100, 20, 400, 60);
        headerPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 4));
        headerPanel.add(headerLabel);

        List<Score> scores = scoreDAO.getList()
            .stream()
            .limit(10)
            .collect(Collectors.toList());
        JPanel labelPanel = new JPanel(null);
        labelPanel.setBounds(0, 0, 540, 300);
        for(int i = 0; i < scores.size(); i++)
        {
            int yPos = (i + 1) * 30;
            Score score = scores.get(i);
            JLabel saveLabel = new JLabel(getScoreStr(i + 1, score));
            saveLabel.setBounds(0, yPos, 500, 20);
            labelPanel.add(saveLabel);
        }
        JPanel scoresContainer = new JPanel(null);
        scoresContainer.setBounds(60, 100, 520, 300);
        scoresContainer.add(labelPanel);

        JButton menuButton = new JButton("Main menu");
        menuButton.setBounds(235, 420, 130, 40);
        menuButton.addActionListener(getMenu());
        this.setTitle(GameParams.APP_TITLE);
        this.setSize(600, 650);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);
        this.add(headerPanel);
        this.add(scoresContainer);
        this.add(menuButton);
    }

    private String getScoreStr(final int number, final Score score)
    {
        return number + ". (" + score.getBoard().getWidth() + "x" +
            score.getBoard().getHeight() + ") " + score.getUsername() +
            " - bombs: " + score.getBoard().getBombQuantity() +
            ", time: " + getTimeStr(score.getGameTime()) +
            ", date: [" + DTF.format(score.getDate()) + "]";
    }

    private String getTimeStr(final GameTime gameTime)
    {
        String minutes = String.format("%02d", gameTime.getMinutes());
        String seconds = String.format("%02d", gameTime.getSeconds());
        return minutes + ":" + seconds;
    }

    private ActionListener getMenu()
    {
        return (e) -> {
            this.dispose();
            new MenuFrame();
        };
    }

}
