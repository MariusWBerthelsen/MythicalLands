package com.mythicallands.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen {
	
	private static JFrame frame;

    public static void main(String[] args) {
        // Create the frame (window) for the login screen
    	frame = new JFrame("Mythical Lands - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);  // Changed to 800x600

        // Create a panel to hold UI components
        JPanel panel = new JPanel();
        frame.add(panel);

        // Place components on the panel
        placeComponents(panel);

        // Set a background color for the panel
        panel.setBackground(Color.LIGHT_GRAY);  // Change as needed

        // Make the frame visible
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // Create username label and text field
        JLabel userLabel = new JLabel("User");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        final JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        // Create password label and text field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        final JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        // Create login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());

                // Simple authentication logic (for demonstration)
                if ("".equals(username) && "".equals(password)) {
                    JOptionPane.showMessageDialog(panel, "Login Successful!");

                    // Hide the login window
                    frame.setVisible(false);
                    frame.dispose();  // Free up system resources

                    // Create and show the game window
                    GameWindow gameWindow = new GameWindow();
                    gameWindow.show();
                } else {
                    JOptionPane.showMessageDialog(panel, "Invalid username or password");
                }
            }
        });
        panel.add(loginButton);
    }
}

