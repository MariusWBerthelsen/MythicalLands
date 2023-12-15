package com.mythicallands.ui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ObjectHandler {
	
	public Map<Integer, GameEntity> loadObjects(String filename) {
	    Map<Integer, GameEntity> objects = new HashMap<>();
	    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            if (line.startsWith("//") || line.trim().isEmpty()) continue; // Skip comments and empty lines
	            String[] parts = line.split("\t");
	            int id = Integer.parseInt(parts[0]);
	            String name = parts[1];
	            boolean interactable = Boolean.parseBoolean(parts[2]);
	            String description = parts[3];
	            objects.put(id, new GameEntity(id, name, interactable, description));
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        // Handle error
	    }
	    return objects;
	}

	public void spawnObjects(String filename, Map<Integer, GameEntity> objects, GameObject[][] objectsLayer, GameObject[][] interactableObjectsLayer) {
	    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            if (line.startsWith("//") || line.trim().isEmpty()) continue;
	            String[] parts = line.split("\t");
	            int id = Integer.parseInt(parts[0]);
	            int x = Integer.parseInt(parts[1]);
	            int y = Integer.parseInt(parts[2]);

	            GameEntity entity = objects.get(id);
	            if (entity != null) {
	                GameObject gameObject = new GameObject(entity, x, y);
	                if (entity.isInteractable()) {
	                    interactableObjectsLayer[x][y] = gameObject;
	                } else {
	                    objectsLayer[x][y] = gameObject;
	                }
	                System.out.println("Spawning " + id + " at X: " + x + ", Y: " + y);
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	
}
