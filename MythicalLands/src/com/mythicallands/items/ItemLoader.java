package com.mythicallands.items;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ItemLoader {
    public Map<Integer, Item> loadItems(String filename) {
        Map<Integer, Item> items = new HashMap<>();
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
        return items;
    }
}