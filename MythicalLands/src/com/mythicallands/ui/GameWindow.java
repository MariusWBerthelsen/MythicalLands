package com.mythicallands.ui;

import javax.swing.*;

public class GameWindow {
    private JFrame frame;

    public GameWindow() {
        frame = new JFrame("Mythical Lands - The Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1120, 840);

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);

        frame.setVisible(false);
    }

    public void show() {
        // This method makes the game window visible
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setSize(1120, 840);
    }
}