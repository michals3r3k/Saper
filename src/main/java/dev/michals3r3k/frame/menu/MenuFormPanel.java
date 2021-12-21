package dev.michals3r3k.frame.menu;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class MenuFormPanel extends JPanel
{
    final Map<JLabel, Component> labelComponentMap;

    MenuFormPanel(Map<JLabel, Component> labelComponentMap)
    {
        this.labelComponentMap = labelComponentMap;
    }

}
