package com.mythicallands.items;

import java.awt.Graphics;
import java.util.Map;

public class Inventory {
    private Item[][] items;
    private final int WIDTH = 4;
    private final int HEIGHT = 7;
    private Map<Integer, Item> itemMap; // Maps item IDs to items

    public Inventory(Map<Integer, Item> itemMap) {
        this.items = new Item[4][7];
        this.itemMap = itemMap;
    }
    
    public boolean isFull() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (items[x][y] == null) {
                    return false; // Found an empty slot, so inventory is not full
                }
            }
        }
        return true; // No empty slots found, inventory is full
    }

    public boolean addItem(int itemId, int amount) {
        int addedCount = 0;
        for (int y = 0; y < HEIGHT && addedCount < amount; y++) {
            for (int x = 0; x < WIDTH && addedCount < amount; x++) {
                if (items[x][y] == null) {
                    items[x][y] = itemMap.get(itemId);
                    if (items[x][y] == null) {
                        System.out.println("Item with ID " + itemId + " not found.");
                        return false; // Item not found
                    }
                    addedCount++;
                }
            }
        }
        if (addedCount == 0) {
            System.out.println("Inventory is full. Could not add any items.");
            return false; // Inventory was full, no items added
        } else if (addedCount < amount) {
            System.out.println("Added " + addedCount + " of " + amount + " items with ID " + itemId + ". Inventory full.");
            return true; // Added some items but not all, due to full inventory
        } else {
            System.out.println("Added " + amount + " items with ID " + itemId + ".");
            return true; // Successfully added all items
        }
    }
    
    public boolean removeItemById(int itemId, int amount) {
        int removedCount = 0;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (items[x][y] != null && items[x][y].getId() == itemId) {
                    items[x][y] = null; // Remove the item
                    removedCount++;
                    if (removedCount >= amount) {
                        System.out.println("Removed " + amount + " items with ID " + itemId + " from inventory.");
                        return true; // Required amount of item was successfully removed
                    }
                }
            }
        }
        if (removedCount > 0) {
            System.out.println("Removed only " + removedCount + " items with ID " + itemId + " from inventory.");
        } else {
            System.out.println("Item with ID " + itemId + " not found in inventory.");
        }
        return false; // Not enough items with the given ID were found
    }
    
    public boolean hasItem(int itemId, int amount) {
        int count = 0;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (items[x][y] != null && items[x][y].getId() == itemId) {
                    count++;
                    if (count >= amount) {
                        return true; // Required amount of item found
                    }
                }
            }
        }
        return false; // Required amount of item not found
    }
    
    public boolean removeItem(int x, int y) {
        if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
            if (items[x][y] != null) {
                items[x][y] = null; // Remove the item
                System.out.println("Item removed from inventory.");
                return true; // Item was successfully removed
            }
        }
        return false; // No item to remove or invalid coordinates
    }

    // Method to draw the inventory on screen
    public void draw(Graphics g, int startX, int startY, int tileSize) {
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 4; x++) {
                if (items[x][y] != null) {
                    // Draw the item at the specified coordinates
                    g.drawImage(items[x][y].getImage(), startX + x * tileSize, startY + y * tileSize, null);
                }
            }
        }
    }
}