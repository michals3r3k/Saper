package dev.michals3r3k.frame.game;

import dev.michals3r3k.frame.menu.GameParams;

import javax.swing.*;
import java.awt.*;

public class FieldPanel extends JPanel
{
    private final int x;
    private final int y;
    String value;
    JLabel label;

    FieldPanel(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.value = null;
        JLabel jLabel = new JLabel();
        jLabel.setVerticalAlignment(JLabel.CENTER);
        jLabel.setHorizontalAlignment(JLabel.CENTER);
        this.label = jLabel;
        setBasicCharacteristics();
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        label.setText(value);
        label.setForeground(Color.BLACK);
        this.value = value;
    }

    public void setLabel(JLabel jLabel)
    {
        this.label = jLabel;
    }

    public void setValue(int value)
    {
        String text = Integer.toString(value);
        label.setText(text);
        label.setForeground(GameParams.TILE_VALUE_COLOR.get(value));
        this.value = Integer.toString(value);
    }

    private void setBasicCharacteristics()
    {
        this.setSize(new Dimension(GameParams.TILE_SIZE, GameParams.TILE_SIZE));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        this.setLayout(new BorderLayout());
        this.setBackground(Color.DARK_GRAY);
        this.add(label);
    }

}
