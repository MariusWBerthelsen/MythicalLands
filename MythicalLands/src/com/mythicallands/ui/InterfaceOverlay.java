package com.mythicallands.ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.mythicallands.items.ItemManager;

public class InterfaceOverlay {
	
	
    private BufferedImage overlayImage;
    private Rectangle closeButtonArea; // Area for the close button
    private boolean visible;
    private ClickableTile[][] tiles;
    private int tileWidth = 50;
    private int tileHeight = 50;
    private Map<Integer, BufferedImage> tileImages;
    private int interfaceX = 150; // X position where the main interface is drawn
    private int interfaceY = 50; // Y position where the main interface is drawn
    private int startX = interfaceX + 25; // Relative X position where tiles start in the interface
    private int startY = interfaceY + 67; // Relative Y position where tiles start in the interface
    
    private int[][] tileItemIDs; // 2D array representing item IDs on each tile

    public boolean isCloseButtonClicked(int x, int y) {
        return closeButtonArea.contains(x, y);
    }
    
    public ClickableTile getTile(int x, int y) {
        if (x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length) {
            return tiles[x][y];
        }
        return null;
    }

    public InterfaceOverlay() {
        // Load the overlay image
        loadImage();

        // Initialize the item IDs for each tile
        tileItemIDs = new int[9][7]; // Assuming a 9x7 grid
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 7; y++) {
                tileItemIDs[x][y] = 0; // Assuming 0 means no item
            }
        }

        // Set the first tile to have item ID 5
        tileItemIDs[0][0] = 5;

        // Initialize and populate the tileImages map
        tileImages = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            tileImages.put(i, ItemManager.getImageForItemID(i));
        }

        // Initialize tiles
        tiles = new ClickableTile[9][7]; // 9x7 grid
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                int itemID = determineItemIDForTile(i, j);
                tiles[i][j] = new ClickableTile(startX + i * tileWidth, startY + j * tileHeight, tileWidth, tileHeight, itemID);
            }
        }

        // Set the close button area (adjust as needed)
        closeButtonArea = new Rectangle(interfaceX + 450, interfaceY + 10, 40, 40); // Example coordinates and size
    }

    private void loadImage() {
        try {
            overlayImage = ImageIO.read(new File("Images/interface.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        if (!visible) return;

        // Draw the overlay image
        if (overlayImage != null) {
            g.drawImage(overlayImage, interfaceX, interfaceY, null);
        }

        // Draw the tiles
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] != null) {
                    tiles[i][j].draw(g);
                }
            }
        }
        
        // Draw item images on tiles
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 7; y++) {
                int itemID = tileItemIDs[x][y];
                if (itemID > 0) {
                    BufferedImage itemImage = tileImages.get(itemID);
                    if (itemImage != null) {
                        // Adjust the coordinates to be relative to the main interface position
                        int tileX = 150 + 25 + x * 50;
                        int tileY = 50 + 67 + y * 50;
                        g.drawImage(itemImage, tileX, tileY, null);
                    }
                }
            }
        }

        // Draw the close button (customize as needed)
        g.setColor(Color.RED); // Example color for the close button
        g.fillRect(closeButtonArea.x, closeButtonArea.y, closeButtonArea.width, closeButtonArea.height);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    // Method to check if a click is on a tile
    public ClickableTile getClickedTile(int mouseX, int mouseY) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] != null && tiles[i][j].isClicked(mouseX, mouseY)) {
                    return tiles[i][j];
                }
            }
        }
        return null;
    }
    
    public int getItemIDForTile(ClickableTile tile) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] == tile) {
                    return tileItemIDs[i][j]; // This array should be initialized with item IDs
                }
            }
        }
        return -1; // Return -1 if no item is associated with the tile
    }
    
    public int determineItemIDForTile(int x, int y) {
        int tileX = (x - 25) / 50; // Calculate tile X position
        int tileY = (y - 67) / 50; // Calculate tile Y position

        if (tileX >= 0 && tileX < 9 && tileY >= 0 && tileY < 7) {
            return tileItemIDs[tileX][tileY]; // Return the item ID at this tile
        }

        return -1; // Return -1 if the coordinates are outside the tile area
    }

    // Additional methods to interact with tiles...
}