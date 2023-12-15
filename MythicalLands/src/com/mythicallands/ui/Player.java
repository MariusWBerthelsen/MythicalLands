package com.mythicallands.ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import com.mythicallands.items.Inventory;
import com.mythicallands.items.Item;
import com.mythicallands.items.ItemLoader;
import com.mythicallands.skills.*;

public class Player {
    private BufferedImage image;
    private int x, y; // Player's position
    private final int WIDTH = 56; // Width of the player
    private final int HEIGHT = 56; // Height of the player
    private int worldX, worldY;
    private int targetX, targetY; // Target world position
    private boolean isMoving = false;
    private boolean canMove = true;
    
    private Woodcutting woodcuttingSkill;
    private Smithing smithingSkill;
    private Mining miningSkill;
    private Inventory inventory;

    public Player(int startX, int startY, String imagePath) {
        this.x = startX; // Initial X position
        this.y = startY; // Initial Y position
        this.worldX = startX;
        this.worldY = startY;
        loadImage(imagePath);
        woodcuttingSkill = new Woodcutting();
        miningSkill = new Mining();
        smithingSkill = new Smithing();
        
        // Initialize inventory with items
        ItemLoader itemLoader = new ItemLoader();
        Map<Integer, Item> allItems = itemLoader.loadItems("Cfg/Items.txt");
        inventory = new Inventory(allItems);
    }
    
    public Inventory getInventory() {
        return inventory;
    }
    
    public void performWoodcutting() {
        woodcuttingSkill.performAction();
        // Additional logic for woodcutting action...
    }
    
    public Woodcutting getWoodcuttingSkill() {
        return woodcuttingSkill;
    }

    // Getters for accessing skill information...
    public int getWoodcuttingLevel() {
        return woodcuttingSkill.getLevel();
    }

    public int getWoodcuttingExperience() {
        return woodcuttingSkill.getExperience();
    }
    
    public void performSmithing() {
        smithingSkill.performAction();
        // Additional logic for woodcutting action...
    }
    
    public Smithing getSmithingSkill() {
        return smithingSkill;
    }

    // Getters for accessing skill information...
    public int getSmithingLevel() {
        return smithingSkill.getLevel();
    }

    public int getSmithingExperience() {
        return smithingSkill.getExperience();
    }
    
    public void performMining() {
        miningSkill.performAction();
        // Additional logic for woodcutting action...
    }
    
    public Mining getMiningSkill() {
        return miningSkill;
    }

    // Getters for accessing skill information...
    public int getMiningLevel() {
        return miningSkill.getLevel();
    }

    public int getMiningExperience() {
        return miningSkill.getExperience();
    }
    
    private void loadImage(String imagePath) {
        try {
            this.image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error, perhaps set a default image or exit
        }
    }

    public void move(int dx, int dy) {
        // Update position
        x += dx;
        y += dy;

        // Boundary checks
        if (x < 0) x = 0; // Left boundary
        if (y < 0) y = 0; // Top boundary
        if (x > 840 - WIDTH) x = 840 - WIDTH; // Right boundary
        if (y > 616 - HEIGHT) y = 616 - HEIGHT; // Bottom boundary
    }
    
    public void moveTo(int centeredTargetX, int centeredTargetY) {
        this.targetX = centeredTargetX - WIDTH / 2;
        this.targetY = centeredTargetY - HEIGHT / 2;
        this.isMoving = true;

        // Calculate horizontal and vertical distances to the target
        int dx = Math.abs(targetX - worldX);
        int dy = Math.abs(targetY - worldY);

        // Determine the initial movement direction based on longer distance
        if (dx > dy) {
            // Move horizontally first
            if (targetX < worldX) {
                worldX--;
            } else {
                worldX++;
            }
        } else {
            // Move vertically first
            if (targetY < worldY) {
                worldY--;
            } else {
                worldY++;
            }
        }
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    

    public int getWorldX() {
        return worldX;
    }

    public int getWorldY() {
        return worldY;
    }
    
    public boolean hasReachedTarget() {
        return worldX == targetX && worldY == targetY;
    }
    
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
    
    public void update() {
    	if (!isMoving || !canMove) return;

        int dx = targetX - worldX;
        int dy = targetY - worldY;

        // Check if the target is reached
        if (dx == 0 && dy == 0) {
            isMoving = false;
            return;
        }

        // Calculate horizontal and vertical distances to the target
        int absDx = Math.abs(dx);
        int absDy = Math.abs(dy);

        if (absDx == absDy) {
            // Diagonal movement
            worldX += Integer.compare(dx, 0);
            worldY += Integer.compare(dy, 0);
        } else if (absDx > absDy) {
            // Move horizontally
            worldX += Integer.compare(dx, 0);
        } else {
            // Move vertically
            worldY += Integer.compare(dy, 0);
        }
    }
    
    public void draw(Graphics g, int screenX, int screenY) {
        if (this.image != null) {
            g.drawImage(this.image, screenX, screenY, null);
        } else {
            // Fallback to drawing a red box if the image failed to load
            g.setColor(Color.RED);
            g.fillRect(screenX, screenY, WIDTH, HEIGHT);
        }
    }
}
