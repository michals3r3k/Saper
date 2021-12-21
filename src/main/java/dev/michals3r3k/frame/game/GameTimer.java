package dev.michals3r3k.frame.game;

import javax.swing.*;
import java.awt.*;

public class GameTimer extends JPanel
{
    int seconds;
    int minutes;
    int elapsedTime;
    String secondsString;
    String minutesString;
    Timer timer;

    GameTimer()
    {
        this.seconds = 0;
        this.minutes = 0;
        this.elapsedTime = 0;
        this.secondsString = String.format("%02d", seconds);
        this.minutesString = String.format("%02d", minutes);
        JLabel timeLabel = new JLabel();
        this.timer = new Timer(1000, e -> {
            elapsedTime += 1000;
            this.seconds = (elapsedTime / 1000) % 60;
            this.minutes = (elapsedTime / 60000) % 60;
            this.secondsString = String.format("%02d", seconds);
            this.minutesString = String.format("%02d", minutes);
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

}
