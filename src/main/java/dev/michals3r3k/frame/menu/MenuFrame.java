package dev.michals3r3k.frame.menu;

import dev.michals3r3k.frame.game.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MenuFrame extends JFrame
{

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

        JLabel nameLabel = new JLabel("Enter name:");
        nameLabel.setBounds(0, 0, 200, 20);
        JLabel colsLabel = new JLabel("Enter board cols:");
        colsLabel.setBounds(0, 30, 200, 20);
        JLabel rowsLabel = new JLabel("Enter board rows:");
        rowsLabel.setBounds(0, 60, 200, 20);
        JLabel difficultyLabel = new JLabel("Select difficulty:");
        difficultyLabel.setBounds(0, 90, 200, 20);
        JPanel labelPanel = new JPanel(null);
        labelPanel.setBounds(0, 0, 200, 130);
        labelPanel.add(nameLabel);
        labelPanel.add(colsLabel);
        labelPanel.add(rowsLabel);
        labelPanel.add(difficultyLabel);

        TextField nameField = new TextField();
        nameField.setBounds(0, 0, 200, 20);
        TextField colsField = new TextField();
        colsField.setBounds(0, 30, 40, 20);
        TextField rowsField = new TextField();
        rowsField.setBounds(0, 60, 40, 20);
        JComboBox difficultyField = new JComboBox(GameParams.DIFFICULTIES);
        difficultyField.setBounds(0, 90, 100, 20);
        JPanel inputPanel = new JPanel(null);
        inputPanel.setBounds(210, 0, 200, 130);
        inputPanel.add(nameField);
        inputPanel.add(colsField);
        inputPanel.add(rowsField);
        inputPanel.add(difficultyField);

        JPanel formContainer = new JPanel(null);
        formContainer.setBounds(60, 170, 410, 130);
        formContainer.add(labelPanel);
        formContainer.add(inputPanel);

        JButton submitButton = new JButton("Start!");
        submitButton.setBounds(200, 330, 130, 40);
        submitButton.addActionListener(
            submitForm(nameField, colsField, rowsField, difficultyField));

        this.setTitle(GameParams.APP_TITLE);
        this.setSize(550, 650);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);
        this.add(headerPanel);
        this.add(formContainer);
        this.add(submitButton);
    }

    private ActionListener submitForm(
        TextField nameField,
        TextField colsField,
        TextField rowsField,
        JComboBox difficultyField)
    {
        return e -> {
            List<FormError> errors = validateFormFields(nameField, colsField, rowsField);
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
            new GameFrame(width, height,
                GameParams.DIFFICULTY_MAP.get(difficultyField.getSelectedItem()));
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
        TextField nameField,
        TextField colsField,
        TextField rowsField)
    {
        List<FormError> errors = new ArrayList<>();
        if(isAnyFieldEmpty(nameField, colsField, rowsField))
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
        TextField nameField,
        TextField colsField,
        TextField rowsField)
    {
        return nameField.getText().isBlank()
            || colsField.getText().isBlank()
            || rowsField.getText().isBlank();
    }

}
