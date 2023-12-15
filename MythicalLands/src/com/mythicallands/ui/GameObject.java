package com.mythicallands.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class GameObject {
    private GameEntity gameEntity;
    private int x; // X position in the world
    private int y; // Y position in the world
    private boolean isWalkable;
    
    public GameObject(GameEntity gameEntity, int tileX, int tileY) {
        this.gameEntity = gameEntity;
        this.x = tileX * GamePanel.TILE_SIZE; // Convert tile index to pixel position
        this.y = tileY * GamePanel.TILE_SIZE;
    }

    public void setWalkable(boolean walkable) {
        this.isWalkable = walkable;
    }

    public boolean isWalkable() {
        return isWalkable;
    }

    public void draw(Graphics g, int screenX, int screenY) {
        BufferedImage image = gameEntity.getImage();
        if (image != null) {
            g.drawImage(image, screenX, screenY, null);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public int getId() {
        return gameEntity.getId();
    }

    public GameEntity getGameEntity() {
        return gameEntity;
    }
}