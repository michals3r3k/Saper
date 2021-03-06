package dev.michals3r3k.frame.menu;

import dev.michals3r3k.context.Context;
import dev.michals3r3k.context.SaveContext;
import dev.michals3r3k.context.UserContext;
import dev.michals3r3k.factory.BoardFactory;
import dev.michals3r3k.frame.LoginFrame;
import dev.michals3r3k.frame.game.GameFrame;
import dev.michals3r3k.model.User;
import dev.michals3r3k.model.save.Save;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MenuFrame extends JFrame
{
    private final BoardFactory boardFactory = new BoardFactory();

    public MenuFrame()
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
        User user = userContext.getUser();
        JLabel nameLabel = new JLabel("Hello " + user.getUsername() + "!");
        nameLabel.setFont(new Font(null, Font.BOLD, 20));
        nameLabel.setBounds(200, 120, 200, 20);
        JLabel colsLabel = new JLabel("Enter board cols:");
        colsLabel.setBounds(0, 30, 200, 20);
        JLabel rowsLabel = new JLabel("Enter board rows:");
        rowsLabel.setBounds(0, 60, 200, 20);
        JLabel difficultyLabel = new JLabel("Select difficulty:");
        difficultyLabel.setBounds(0, 90, 200, 20);
        JPanel labelPanel = new JPanel(null);
        labelPanel.setBounds(0, 0, 200, 130);
        labelPanel.add(colsLabel);
        labelPanel.add(rowsLabel);
        labelPanel.add(difficultyLabel);

        TextField colsField = new TextField();
        colsField.setBounds(0, 30, 40, 20);
        TextField rowsField = new TextField();
        rowsField.setBounds(0, 60, 40, 20);
        JComboBox difficultyField = new JComboBox(GameParams.DIFFICULTIES);
        difficultyField.setBounds(0, 90, 100, 20);
        JPanel inputPanel = new JPanel(null);
        inputPanel.setBounds(210, 0, 200, 130);
        inputPanel.add(colsField);
        inputPanel.add(rowsField);
        inputPanel.add(difficultyField);

        JPanel formContainer = new JPanel(null);
        formContainer.setBounds(60, 170, 410, 130);
        formContainer.add(labelPanel);
        formContainer.add(inputPanel);

        JButton submitButton = new JButton("Start!");
        submitButton.setBounds(200, 300, 130, 40);
        submitButton.addActionListener(
            submitForm(colsField, rowsField, difficultyField));

        JButton loadButton = new JButton("Load game");
        loadButton.setBounds(200, 350, 130, 40);
        loadButton.addActionListener(getLoadFrame());

        JButton scoreBoardButton = new JButton("Score board");
        scoreBoardButton.setBounds(200, 400, 130, 40);
        scoreBoardButton.addActionListener(getScoreBoardFrame());

        JButton logoutButton = new JButton("Log out");
        logoutButton.setBounds(200, 450, 130, 40);
        logoutButton.addActionListener(logout());

        this.add(nameLabel);
        this.setTitle(GameParams.APP_TITLE);
        this.setSize(550, 650);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);
        this.add(headerPanel);
        this.add(formContainer);
        this.add(submitButton);
        this.add(loadButton);
        this.add(scoreBoardButton);
        this.add(logoutButton);
    }

    private ActionListener getScoreBoardFrame()
    {
        return e -> {
            this.dispose();
            new ScoreBoardFrame();
        };
    }

    private ActionListener logout()
    {
        return e->{
            Context context = Context.getContext();
            SaveContext saveContext = SaveContext.getSaveContext(context);
            saveContext.setSave(null);
            UserContext userContext = UserContext.getUserContext(context);
            userContext.setUser(null);
            this.dispose();
            new LoginFrame();
        };
    }

    private ActionListener getLoadFrame()
    {
        return e -> {
            this.dispose();
            new LoadFrame();
        };
    }

    private ActionListener submitForm(
        TextField colsField,
        TextField rowsField,
        JComboBox difficultyField)
    {
        return e -> {
            List<FormError> errors = validateFormFields(colsField, rowsField);
            if(!errors.isEmpty())
            {
                String errorMessage = prepareErrorMessage(errors);
                JOptionPane.showMessageDialog(null, errorMessage,
                    "Form submit failure", JOptionPane.ERROR_MESSAGE);
                return;
            }
            this.dispose();
            int width = Integer.parseInt(colsField.getText());
            int height = Integer.parseInt(rowsField.getText());
            int saturation = GameParams.DIFFICULTY_MAP.get(
                (String) difficultyField.getSelectedItem());
            Save save = new Save(boardFactory.getBoard(height, width, saturation));
            Context context = Context.getContext();
            SaveContext saveContext = SaveContext.getSaveContext(context);
            saveContext.setSave(save);
            new GameFrame(save);
        };
    }

    private String prepareErrorMessage(List<FormError> errors)
    {
        StringBuilder sb = new StringBuilder("Form is containing errors given below:");
        if(errors.contains(FormError.IS_NOT_DIGIT))
        {
            sb.append("\n\t* Width and height have to be numeric");
        }
        if(errors.contains(FormError.NOT_ALL_FILLED))
        {
            sb.append("\n\t* All fields should be filled");
        }
        return sb.toString();
    }

    private List<FormError> validateFormFields(
        TextField colsField,
        TextField rowsField)
    {
        List<FormError> errors = new ArrayList<>();
        if(isAnyFieldEmpty(colsField, rowsField))
        {
            errors.add(FormError.NOT_ALL_FILLED);
        }
        if(!isNumberFieldsValid(colsField, rowsField))
        {
            errors.add(FormError.IS_NOT_DIGIT);
        }
        return errors;
    }

    private boolean isNumberFieldsValid(TextField colsField, TextField rowsField)
    {
        for(char c : colsField.getText().toCharArray())
        {
            if(!Character.isDigit(c))
            {
                return false;
            }
        }
        for(char c : rowsField.getText().toCharArray())
        {
            if(!Character.isDigit(c))
            {
                return false;
            }
        }
        return true;
    }

    private boolean isAnyFieldEmpty(
        TextField colsField,
        TextField rowsField)
    {
        return colsField.getText().isBlank()
            || rowsField.getText().isBlank();
    }

}
