import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Drew Carrillo
 * CEN 3024C
 * 03/10/2026
 * Inventory Manager
 * This is the Business Logic layer. It manages the ArrayList of VideoGame objects.
 * Contains all CRUD operations and the custom calculation (away from the UI).
 * The UI (MainApp) calls these methods to perform actions on the data.
 */
public class InventoryManager {
    // This is our "database" - an ArrayList to store all VideoGame objects
    private ArrayList<VideoGame> inventory;

    //New Inventory Constructor!
    public InventoryManager() {
        inventory = new ArrayList<>();
    }

    /**
     * Helper method for unit testing - returns the current size of inventory
     * returns The number of games in the inventory
     */
    public int getInventorySize() {
        return inventory.size();
    }

    /**
     * Reads game data from a text file and populates the inventory
     * Each line is supposed to be in format: ID, Title, Platform, Year, Price, Multiplayer
     *  filepath =  Location of the text file
     * returns true if file was read successfully, false if file not found
     */
    public boolean loadDataFromFile(String filepath) {
        try {
            File file = new File(filepath);
            Scanner fileScanner = new Scanner(file);

            // Read each line of the file
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");

                // Make sure line has all 6 required fields
                if (parts.length == 6) {
                    try {
                        // Parse each field from the line
                        int id = Integer.parseInt(parts[0].trim());
                        String title = parts[1].trim();
                        String platform = parts[2].trim();
                        int year = Integer.parseInt(parts[3].trim());
                        double price = Double.parseDouble(parts[4].trim());
                        boolean multi = Boolean.parseBoolean(parts[5].trim());

                        // Only add if ID doesn't already exist (avoid duplicates)
                        if (findGameById(id) == null) {
                            inventory.add(new VideoGame(id, title, platform, year, price, multi));
                        }
                    } catch (NumberFormatException e) {
                        // Skip lines with invalid number formats
                        System.out.println("Warning: Skipping invalid line: " + line);
                    }
                }
            }
            fileScanner.close();
            return true; // File was successfully read
        } catch (FileNotFoundException e) {
            return false; // File not found
        } catch (Exception e) {
            // Catch any other unexpected errors
            System.out.println("Warning: Unexpected error reading file: " + e.getMessage());
            return true; // Still return true for partial success
        }
    }

    /**
     * Adds a new game to the inventory
     *  game The VideoGame object to add
     *  true if added successfully, false if ID already exists
     */
    public boolean addGame(VideoGame game) {
        // Check if game with this ID already exists
        if (findGameById(game.getGameID()) != null) {
            return false; // Duplicate ID found
        }
        inventory.add(game);
        return true; // Successfully added
    }

    /**
     * Removes a game from the inventory by ID
     *  id The ID of the game to remove
     *
     */
    public boolean removeGame(int id) {
        VideoGame target = findGameById(id);
        if (target != null) {
            inventory.remove(target);
            return true; // Successfully removed
        }
        return false; // ID not found
    }

    /**
     * Returns a formatted string of all games in the inventory
     * @return String containing all game information, or empty message if no games
     */
    public String displayInventory() {
        if (inventory.isEmpty()) {
            return "The inventory is currently empty.";
        }

        // Build a string with all game information
        StringBuilder sb = new StringBuilder();
        for (VideoGame game : inventory) {
            sb.append(game.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * CUSTOM ACTION: Calculates the total dollar value of all games in inventory
     * This is the mathematical calculation required by the assignment
     *
     */
    public double calculateTotalValue() {
        double total = 0.0;
        // Loop through all games and add up their prices
        for (VideoGame game : inventory) {
            total += game.getPrice();
        }
        return total;
    }

    /**
     * Helper method to find a game by its ID
     * return The VideoGame object if found, null if not found
     */
    public VideoGame findGameById(int id) {
        // Linear search through the ArrayList
        for (VideoGame game : inventory) {
            if (game.getGameID() == id) {
                return game; // Found it!
            }
        }
        return null; // Not found after checking all games
    }
}