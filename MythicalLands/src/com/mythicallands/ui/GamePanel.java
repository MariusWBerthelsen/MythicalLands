package com.mythicallands.ui;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.mythicallands.items.ItemManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class GamePanel extends JPanel implements ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private Runnable pendingInteraction = null;
	
	private int logs = 0; // Tracks the number of logs collected
	private BufferedImage menuImage;
    private Timer gameTimer;
    private Player player;
    private InterfaceOverlay interfaceOverlay;
    public static final int TILE_SIZE = 56;
    
    public static final int WORLD_LAYER = 0;
    public static final int OBJECTS_LAYER = 1;
    public static final int INTERACTABLE_OBJECTS_LAYER = 2;
    
    // Define variables to store game objects for each layer
    private GameObject[][] worldLayer;
    private GameObject[][] objectsLayer;
    private GameObject[][] interactableObjectsLayer;
    
    private static final int PLAYER_SCREEN_X = 840 / 2 - 56 / 2;
    private static final int PLAYER_SCREEN_Y = 616 / 2 - 56 / 2;

    public GamePanel() {
        int width = 15;
        int height = 11;
        
        // Example values for starting position and image path
        int playerStartX = 100; // Starting X position for the player
        int playerStartY = 100; // Starting Y position for the player
        String playerImagePath = "Images/player.png"; // Path to the player image
        
        ItemManager.loadItems("Cfg/Items.txt");

        // Initialize the player
        player = new Player(playerStartX, playerStartY, playerImagePath);

        // Initialize only once
        worldLayer = new GameObject[width][height];
        objectsLayer = new GameObject[width][height];
        interactableObjectsLayer = new GameObject[width][height];

        ObjectHandler objectHandler = new ObjectHandler();
        Map<Integer, GameEntity> gameEntities = objectHandler.loadObjects("Cfg/Objects.txt");
        objectHandler.spawnObjects("Cfg/ObjectSpawn.txt", gameEntities, objectsLayer, interactableObjectsLayer);
    	
        // Initialize the game timer
        gameTimer = new Timer(16, this); // Approx. 60 FPS
        gameTimer.start();

        // Load images and initialize game elements
        loadImages();
        initializePlayer();

        // Set panel properties
        setFocusable(true); // To receive key events
        requestFocusInWindow();

        // Initialize layers
        initializeLayers();
        
        interfaceOverlay = new InterfaceOverlay();

        // Handle mouse clicks
        addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        	    if (interfaceOverlay.isVisible()) {

                    int relativeX = e.getX() - 150; // Adjust x-coordinate relative to the interface's position
                    int relativeY = e.getY() - 50;  // Adjust y-coordinate relative to the interface's position
                    
        	        if (interfaceOverlay.isCloseButtonClicked(e.getX(), e.getY())) {
        	            interfaceOverlay.setVisible(false);
        	            player.setCanMove(true); // Re-enable player movement when the interface is closed
                        return; // Exit to avoid further processing
        	        }
    	            // Handle tile clicks
    	            ClickableTile clickedTile = interfaceOverlay.getClickedTile(e.getX(), e.getY());
    	            if (clickedTile != null) {
    	                if (clickedTile == interfaceOverlay.getTile(0, 0)) {
    	                    System.out.println("First tile was clicked.");
    	                    if(player.getInventory().hasItem(4, 5)) {
    	                    	player.getInventory().removeItemById(4, 5);
    	                    	player.getSmithingSkill().smithItem("Bronze Platebody");
    	                    	player.getInventory().addItem(5);
    	                    } else {
    	                    	System.out.println("You don't have the required items.");
    	                    }
    	                }
        	        }
        	    } else {
        	        if (SwingUtilities.isRightMouseButton(e)) {
        	            handleRightClick(e);
        	        } else {
        	            int worldX = e.getX() + player.getWorldX() - PLAYER_SCREEN_X;
        	            int worldY = e.getY() + player.getWorldY() - PLAYER_SCREEN_Y;

        	            GameObject clickedObject = clickObject(e.getX(), e.getY());
        	            if (clickedObject != null) {
        	                int objectTileX = clickedObject.getX() / TILE_SIZE;
        	                int objectTileY = clickedObject.getY() / TILE_SIZE;
        	                int playerTileX = player.getWorldX() / TILE_SIZE;
        	                int playerTileY = player.getWorldY() / TILE_SIZE;

        	                Point nearestTile = findNearestWalkableTile(objectTileX, objectTileY, playerTileX, playerTileY);
        	                if (nearestTile != null) {
        	                    movePlayerTo(nearestTile.x * TILE_SIZE, nearestTile.y * TILE_SIZE, () -> interactWithObject(clickedObject));
        	                }
        	            } else {
        	                movePlayerTo(worldX, worldY, null); // Move without interaction
        	            }
        	        }
        	    }
        	}
            
            private void handleRightClick(MouseEvent e) {
                int startX = 880; // Inventory starting X position
                int startY = 390; // Inventory starting Y position
                int tileSize = 50; // Tile size of inventory items

                int tileX = (e.getX() - startX) / tileSize;
                int tileY = (e.getY() - startY) / tileSize;

                player.getInventory().removeItem(tileX, tileY);
            }

            private void handleLeftClick(MouseEvent e) {
                // Existing left-click interaction logic
            }

            private void interactWithObject(GameObject object) {
                int objectId = object.getId();
                GameEntity entity = object.getGameEntity();
                switch (objectId) {
                    case 1: // Tree
                        player.getWoodcuttingSkill().performAction(); // Perform woodcutting action
                        player.getInventory().addItem(1);
                        System.out.println(entity.getName() + " clicked: logs = " + logs + ". " + entity.getDescription());
                        break;
                    case 2: // Copper
                        player.getMiningSkill().mineOre("Copper");
                        player.getInventory().addItem(2);
                        break;
                    case 3: // Tin
                        player.getMiningSkill().mineOre("Tin");
                        player.getInventory().addItem(3);
                        break;
                    case 4: // Furnace
                    	if(player.getInventory().hasItem(2, 1) && player.getInventory().hasItem(3, 1)) {
                    		player.getInventory().removeItemById(2, 1);
                    		player.getInventory().removeItemById(3, 1);
                    		player.getInventory().addItem(4);
                    		System.out.println("You made a bronze bar");
                    	} else {
                    		System.out.println("You don't have the required ore.");
                    	}
                    	break;
                    case 6: // Anvil
                    	interfaceOverlay.setVisible(true);
                    	player.setCanMove(false);
                    	break;
                    // ... other cases ...
                    default:
                        break;
                }
            }
        });
    }
    
    private BufferedImage loadWaterImage() {
        try {
            return ImageIO.read(new File("Images/water.png"));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error, possibly with a placeholder image
            return null;
        }
    }

    
    public GameObject clickObject(int mouseX, int mouseY) {
        // Convert screen coordinates to world coordinates
        int worldX = mouseX + player.getWorldX() - PLAYER_SCREEN_X;
        int worldY = mouseY + player.getWorldY() - PLAYER_SCREEN_Y;
        
        // Convert world coordinates to tile coordinates
        int tileX = worldX / TILE_SIZE;
        int tileY = worldY / TILE_SIZE;

        // Check bounds to ensure coordinates are within the game world
        if (tileX < 0 || tileY < 0 || tileX >= worldLayer.length || tileY >= worldLayer[0].length) {
            return null;
        }

        // Check for an object in the interactable objects layer
        if (interactableObjectsLayer[tileX][tileY] != null) {
            return interactableObjectsLayer[tileX][tileY];
        }

        // Check for an object in the regular objects layer
        if (objectsLayer[tileX][tileY] != null) {
            return objectsLayer[tileX][tileY];
        }

        // No object at the clicked location
        return null;
    }
    
 // Method to move player to a new position
    public void movePlayerTo(int targetWorldX, int targetWorldY, Runnable onCompletion) {
        System.out.println("Clicked world coordinates: " + targetWorldX + " " + targetWorldY);

        // Adjust coordinates to handle edge cases
        int tileX = (targetWorldX < 0 && targetWorldX > -TILE_SIZE) ? -1 : targetWorldX / TILE_SIZE;
        int tileY = (targetWorldY < 0 && targetWorldY > -TILE_SIZE) ? -1 : targetWorldY / TILE_SIZE;
        
        if (tileX < 0 || tileY < 0 || tileX >= worldLayer.length || tileY >= worldLayer[0].length) {
            return;
        }

        if (isTileWalkable(tileX, tileY)) {
            player.moveTo(tileX * TILE_SIZE + TILE_SIZE / 2, tileY * TILE_SIZE + TILE_SIZE / 2);
            System.out.println("Walking to: " + tileX + " " + tileY);
        } else {
            System.out.println("Tile not walkable: " + tileX + " " + tileY);
        }
        this.pendingInteraction = onCompletion;
    }


    private void loadImages() {
        try {
            menuImage = ImageIO.read(new File("Images/menu.png"));
            // Load other images here if needed
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error, e.g., use a placeholder image or exit
        }
    }

    private void initializePlayer() {
        // Initialize your player here, set starting position, etc.
        player = new Player(100, 100, "images/player.png");
    }
    
    private void initializeLayers() {
        // Initialize the world layer with grass tiles
        GameEntity grassEntity = new GameEntity(1, "Grass", false, "A grass tile"); // Example
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 11; j++) {
                GameObject grassTile = new GameObject(grassEntity, i, j);
                grassTile.setWalkable(true);
                worldLayer[i][j] = grassTile;
            }
        }
        // ... other layer initializations ...
    }
    
    private Point findNearestWalkableTile(int objectX, int objectY, int playerTileX, int playerTileY) {
        Point nearestTile = null;
        double minDistance = Double.MAX_VALUE;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // left, right, up, down
        for (int[] dir : directions) {
            int newX = objectX + dir[0];
            int newY = objectY + dir[1];

            // Check bounds
            if (newX >= 0 && newX < worldLayer.length && newY >= 0 && newY < worldLayer[0].length) {
                // Check if the tile is walkable
                if (isTileWalkable(newX, newY)) {
                    double distance = Math.sqrt(Math.pow(newX - playerTileX, 2) + Math.pow(newY - playerTileY, 2));
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestTile = new Point(newX, newY);
                    }
                }
            }
        }
        return nearestTile; // May be null if no walkable tile is found
    }
    
    public boolean isTileWalkable(int x, int y) {
        // Check if the base tile is walkable
        if (!worldLayer[x][y].isWalkable()) {
            return false;
        }

        // Check if there is an object on the tile and if it's walkable
        if ((objectsLayer[x][y] != null && !objectsLayer[x][y].isWalkable()) || (interactableObjectsLayer[x][y] != null && !interactableObjectsLayer[x][y].isWalkable())) {
            return false;
        }

        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Load water image
        BufferedImage waterImage = loadWaterImage();

        // Calculate the offset to render the world
        int worldOffsetX = player.getWorldX() - PLAYER_SCREEN_X;
        int worldOffsetY = player.getWorldY() - PLAYER_SCREEN_Y;
        
        // Define the boundaries for the water tiles
        int waterBoundary = 14; // Number of tiles for the water boundary
        
        // Render the game world with water around it
        for (int i = -waterBoundary; i < worldLayer.length + waterBoundary; i++) {
            for (int j = -waterBoundary; j < worldLayer[0].length + waterBoundary; j++) {
                if (i < 0 || j < 0 || i >= worldLayer.length || j >= worldLayer[0].length) {
                    // Draw water tile
                    g.drawImage(waterImage, i * TILE_SIZE - worldOffsetX, j * TILE_SIZE - worldOffsetY, this);
                } else {
                    // Draw regular world tile
                    GameObject obj = worldLayer[i][j];
                    if (obj != null) {
                        int objScreenX = obj.getX() - worldOffsetX;
                        int objScreenY = obj.getY() - worldOffsetY;
                        obj.draw(g, objScreenX, objScreenY);
                    }
                }
            }
        }

        // Render the World Layer
        renderLayer(g, worldLayer, worldOffsetX, worldOffsetY);
        
        renderLayer2(g, objectsLayer, worldOffsetX, worldOffsetY);
        renderLayer2(g, interactableObjectsLayer, worldOffsetX, worldOffsetY);
        
        // Player
        player.draw(g, PLAYER_SCREEN_X, PLAYER_SCREEN_Y);

        // Draw the menu or UI elements
        if (menuImage != null) {
            g.drawImage(menuImage, 0, 0, this);
        }
        
        player.getInventory().draw(g, 880, 390, 50);
        
        interfaceOverlay.draw(g);
    }
    
    private void renderLayer2(Graphics g, GameObject[][] layer, int worldOffsetX, int worldOffsetY) {
        for (int i = 0; i < layer.length; i++) {
            for (int j = 0; j < layer[i].length; j++) {
                if (layer[i][j] != null) {
                    int objScreenX = i * TILE_SIZE - worldOffsetX;
                    int objScreenY = j * TILE_SIZE - worldOffsetY;
                    layer[i][j].draw(g, objScreenX, objScreenY);
                }
            }
        }
    }
    
    // Define a method to add game objects to the layers
    public void addObjectToLayer(GameObject object, int x, int y, int layer) {
        if (x >= 0 && x < 15 && y >= 0 && y < 11) {
            if (layer == WORLD_LAYER) {
                worldLayer[x][y] = object;
            } else if (layer == OBJECTS_LAYER) {
                objectsLayer[x][y] = object;
            } else if (layer == INTERACTABLE_OBJECTS_LAYER) {
                interactableObjectsLayer[x][y] = object;
            }
        }
    }

    private void renderLayer(Graphics g, GameObject[][] layer, int offsetX, int offsetY) {
        for (int i = 0; i < layer.length; i++) {
            for (int j = 0; j < layer[0].length; j++) {
                GameObject obj = layer[i][j];
                if (obj != null) {
                    int objScreenX = obj.getX() - offsetX;
                    int objScreenY = obj.getY() - offsetY;
                    obj.draw(g, objScreenX, objScreenY);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Update game logic, player, objects, etc.
        player.update();
        if (player.hasReachedTarget() && pendingInteraction != null) {
            pendingInteraction.run(); // Execute the stored action
            pendingInteraction = null; // Clear the action
        }
        repaint(); // Redraw the game panel
    }
}
