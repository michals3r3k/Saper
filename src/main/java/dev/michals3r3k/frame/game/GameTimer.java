package dev.michals3r3k.frame.game;

import dev.michals3r3k.model.save.GameTime;

import javax.swing.*;
import java.awt.*;

public class GameTimer extends JPanel
{
    private final GameTime gameTime;
    private final Timer timer;
    private String secondsString;
    private String minutesString;

    GameTimer(GameTime time)
    {
        this.gameTime = time;
        this.secondsString = String.format("%02d", time.getSeconds());
        this.minutesString = String.format("%02d", time.getMinutes());
        JLabel timeLabel = new JLabel();
        this.timer = new Timer(1000, e -> {
            gameTime.addSecond();
            this.secondsString = String.format("%02d", gameTime.getSeconds());
            this.minutesString = String.format("%02d", gameTime.getMinutes());
            timeLabel.setText(minutesString + ":" + secondsString);
        });
        timeLabel.setText(minutesString + ":" + secondsString);
        timeLabel.setBorder(BorderFactory.createBevelBorder(1));
        timeLabel.setFont(new Font("Verdana", Font.PLAIN, 25));
        timeLabel.setOpaque(true);
        timeLabel.setHorizontalAlignment(JTextField.CENTER);
        timeLabel.setBounds(0, 0, 160, 50);

        this.add(timeLabel);
        this.setLayout(null);
    }

    public Timer getTimer()
    {
        return timer;
    }

    public GameTime getGameTime()
    {
        return gameTime;
    }

}
