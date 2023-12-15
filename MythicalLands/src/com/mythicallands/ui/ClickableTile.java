package com.mythicallands.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.mythicallands.items.ItemManager;

public class ClickableTile {
    private int x, y; // Position of the tile
    private int width, height; // Size of the tile
    private BufferedImage image; // Image to display

    public ClickableTile(int x, int y, int width, int height, int itemID) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = ItemManager.getImageForItemID(itemID); // Assuming ItemManager can return images for IDs
    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, null);
        }
    }

    public boolean isClicked(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    // Getters for x and y
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}