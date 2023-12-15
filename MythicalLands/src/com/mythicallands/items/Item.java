package com.mythicallands.items;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Item {
    private int id;
    private String name;
    private BufferedImage image;

    public Item(int id, String name) {
        this.id = id;
        this.name = name;
        loadImage("Images/" + name + ".png");
    }

    private void loadImage(String imagePath) {
        try {
            this.image = ImageIO.read(new File(imagePath));
            System.out.println("Loaded image for " + name + " successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load image for " + name);
        }
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BufferedImage getImage() {
        return image;
    }
}