package com.mythicallands.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GameEntity {
    private int id;
    private String name;
    private boolean interactable;
    private String description;
    private BufferedImage image;

    public GameEntity(int id, String name, boolean interactable, String description) {
        this.id = id;
        this.name = name;
        this.interactable = interactable;
        this.description = description;
        loadImage(name);
    }

    private void loadImage(String name) {
        try {
            this.image = ImageIO.read(new File("Images/" + name + ".png"));
            System.out.println("Loaded image for: " + name); // Confirm image loading
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading image for: " + name);
        }
    }
    
    public BufferedImage getImage() {
        return image;
    }

    public boolean isInteractable() {
        return interactable;
    }
    
    public String getName() {
        return name;
    }
    
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    // Getters and other methods...
}