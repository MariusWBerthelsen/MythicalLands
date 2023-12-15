package com.mythicallands.items;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ItemManager {
    private static Map<Integer, Item> items = new HashMap<>();

    public static void loadItems(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("//") || line.trim().isEmpty()) continue;
                String[] parts = line.split("\t");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                items.put(id, new Item(id, name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Item getItem(int id) {
        return items.get(id);
    }

    public static BufferedImage getImageForItemID(int itemID) {
        Item item = items.get(itemID);
        if (item != null) {
            return item.getImage();
        }
        return null; // or a default image
    }
}