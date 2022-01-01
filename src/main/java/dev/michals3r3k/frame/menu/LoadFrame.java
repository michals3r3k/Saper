package dev.michals3r3k.frame.menu;

import dev.michals3r3k.context.Context;
import dev.michals3r3k.context.SaveContext;
import dev.michals3r3k.context.UserContext;
import dev.michals3r3k.dao.SaveDAO;
import dev.michals3r3k.dao.SaveREPO;
import dev.michals3r3k.dao.Saveable;
import dev.michals3r3k.frame.game.GameFrame;
import dev.michals3r3k.model.save.Save;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class LoadFrame extends JFrame
{
    private final static DateTimeFormatter DTF =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final SaveDAO saveDAO = new SaveDAO();
    Saveable saveREPO = new SaveREPO();

    public LoadFrame()
    {
        JLabel headerLabel = new JLabel("SAPER BY MICHAL S");
        headerLabel.setBounds(20, 0, 500, 60);
        headerLabel.setFont(new Font(null, Font.BOLD, 30));
        headerLabel.setForeground(Color.DARK_GRAY);
        JPanel headerPanel = new JPanel(null);
        headerPanel.setBounds(70, 20, 400, 60);
        headerPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 4));
        headerPanel.add(headerLabel);

        Context context = Context.getContext();
        UserContext userContext = UserContext.getUserContext(context);
        List<Save> saves = saveDAO.getSavesByUsername(userContext.getCurrentUserName())
            .stream()
            .limit(5)
            .collect(Collectors.toList());
        JPanel labelPanel = new JPanel(null);
        labelPanel.setBounds(0, 0, 200, 300);
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(210, 0, 210, 300);
        for(int i = 0; i < saves.size(); i++)
        {
            int yPos = (i + 1) * 50;
            Save save = saves.get(i);
            String labelStr = "Save " + i + ": " + getDateTimeString(save.getSaveTime());
            JLabel saveLabel = new JLabel(labelStr);
            saveLabel.setBounds(0, yPos, 200, 40);
            labelPanel.add(saveLabel);

            JButton loadButton = new JButton("Load");
            loadButton.setBounds(0, yPos, 100, 40);
            loadButton.addActionListener(loadGame(save));
            buttonPanel.add(loadButton);

            JButton deleteButton = new JButton("Remove");
            deleteButton.setBounds(110, yPos, 100, 40);
            deleteButton.addActionListener(deleteSave(save));
            buttonPanel.add(deleteButton);
        }
        JPanel savesContainer = new JPanel(null);
        savesContainer.setBounds(60, 100, 420, 300);
        savesContainer.add(labelPanel);
        savesContainer.add(buttonPanel);

        JButton menuButton = new JButton("Main menu");
        menuButton.setBounds(220, 420, 130, 40);
        menuButton.addActionListener(getMenu());
        this.setTitle(GameParams.APP_TITLE);
        this.setSize(550, 650);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);
        this.add(headerPanel);
        this.add(savesContainer);
        this.add(menuButton);
    }

    private ActionListener getMenu()
    {
        return (e) -> {
            this.dispose();
            new MenuFrame();
        };
    }

    private ActionListener deleteSave(final Save save)
    {
        return e -> {
            saveREPO.remove(save);
            this.dispose();
            new LoadFrame();
        };
    }

    private ActionListener loadGame(final Save save)
    {
        return (e) -> {
            this.dispose();
            Context context = Context.getContext();
            SaveContext saveContext = SaveContext.getSaveContext(context);
            saveContext.setSave(save);
            new GameFrame(save);
        };
    }

    private String getDateTimeString(LocalDateTime date)
    {
        return DTF.format(date);
    }

}
