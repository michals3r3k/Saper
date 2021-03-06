package dev.michals3r3k.frame;

import dev.michals3r3k.context.Context;
import dev.michals3r3k.context.SaveContext;
import dev.michals3r3k.context.UserContext;
import dev.michals3r3k.dao.user.UserDAO;
import dev.michals3r3k.dao.user.UserREPO;
import dev.michals3r3k.frame.menu.GameParams;
import dev.michals3r3k.frame.menu.MenuFrame;
import dev.michals3r3k.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoginFrame extends JFrame
{
    private final UserDAO userDAO = new UserDAO();
    private final UserREPO userREPO = new UserREPO();

    public LoginFrame()
    {
        JLabel headerLabel = new JLabel("SAPER BY MICHAL S");
        headerLabel.setBounds(20, 0, 500, 60);
        headerLabel.setFont(new Font(null, Font.BOLD, 30));
        headerLabel.setForeground(Color.DARK_GRAY);
        JPanel headerPanel = new JPanel(null);
        headerPanel.setBounds(70, 20, 400, 60);
        headerPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 4));
        headerPanel.add(headerLabel);

        JLabel nameLabel = new JLabel("Enter username:");
        nameLabel.setBounds(0, 0, 200, 20);
        JLabel passwordLabel = new JLabel("Enter password:");
        passwordLabel.setBounds(0, 30, 200, 20);
        JPanel labelPanel = new JPanel(null);
        labelPanel.setBounds(0, 0, 200, 130);
        labelPanel.add(nameLabel);
        labelPanel.add(passwordLabel);

        TextField nameField = new TextField();
        nameField.setBounds(0, 0, 200, 20);
        TextField passwordField = new TextField();
        passwordField.setBounds(0, 30, 200, 20);
        JPanel inputPanel = new JPanel(null);
        inputPanel.setBounds(210, 0, 200, 130);
        inputPanel.add(nameField);
        inputPanel.add(passwordField);

        JPanel formContainer = new JPanel(null);
        formContainer.setBounds(60, 170, 410, 130);
        formContainer.add(labelPanel);
        formContainer.add(inputPanel);

        JButton loginButton = new JButton("Log in");
        loginButton.setBounds(200, 330, 130, 40);
        loginButton.addActionListener(
            login(nameField, passwordField));

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(200, 390, 130, 40);
        registerButton.addActionListener(
            register(nameField, passwordField));

        this.setTitle(GameParams.APP_TITLE);
        this.setSize(550, 650);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);
        this.add(headerPanel);
        this.add(formContainer);
        this.add(loginButton);
        this.add(registerButton);
    }

    private ActionListener login(TextField loginField, TextField passwordField)
    {
        return (e) -> {
            String username = loginField.getText();
            String password = passwordField.getText();
            List<LoginError> errors = validateLogin(username, password);
            if(!errors.isEmpty())
            {
                String errorMessage = prepareErrorMessage(errors);
                JOptionPane.showMessageDialog(null, errorMessage,
                    "Login failure", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                this.dispose();
                Context context = Context.getContext();
                UserContext userContext = UserContext.getUserContext(context);
                userContext.setUser(userDAO.getUser(username));
                context.setParameter(userContext);
                SaveContext saveContext = SaveContext.getSaveContext(context);
                context.setParameter(saveContext);
                new MenuFrame();
            }
        };
    }

    private List<LoginError> validateLogin(String username, String password)
    {
        List<LoginError> result = new ArrayList<>();
        User user = userDAO.getUser(username);
        if(user == null)
        {
            result.add(LoginError.NOT_EXISTS);
            return result;
        }
        if(!user.getPassword().equals(password))
        {
            result.add(LoginError.INVALID_PASSWORD);
        }
        return result;
    }

    private ActionListener register(TextField loginField, TextField passwordField)
    {
        return (e) -> {
            String username = loginField.getText();
            String password = passwordField.getText();
            List<RegisterError> errors = validateRegister(username);
            if(!errors.isEmpty())
            {
                String errorMessage = prepareErrorMessage(errors);
                JOptionPane.showMessageDialog(null, errorMessage,
                    "Register failure", JOptionPane.ERROR_MESSAGE);
            } else
            {
                userREPO.save(new User(username, password));
            }
        };
    }

    private List<RegisterError> validateRegister(String username)
    {
        User user = userDAO.getUser(username);
        if(user != null)
        {
            return List.of(RegisterError.ACCOUNT_ALREADY_EXIST);
        }
        return Collections.emptyList();
    }

    private <E> String prepareErrorMessage(List<E> errors)
    {
        StringBuilder sb = new StringBuilder("Form is containing errors given below:");
        if(errors.contains(LoginError.NOT_EXISTS))
        {
            sb.append("\n\t* Account doesn't exists");
        }
        if(errors.contains(LoginError.INVALID_PASSWORD))
        {
            sb.append("\n\t* Invalid password");
        }
        if(errors.contains(RegisterError.ACCOUNT_ALREADY_EXIST))
        {
            sb.append("\n\t* Account already exists");
        }
        return sb.toString();
    }

}
