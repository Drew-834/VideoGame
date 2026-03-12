import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Drew Carrillo
 * CEN 3024C
 * 03/10/2026
 * Inventory Manager
 * This is the Business Logic layer, It manages the ArrayList of the VideoGame objects themselves.
 * Also all the CRUD operations and the custom calculation (all away from the UI)
 */
public class InventoryManager {
    private ArrayList<VideoGame> inventory;
    public InventoryManager() {
        inventory = new ArrayList<>();
    }

    /**
     * This reads the text file and populates inventory.
     * @param filepath Location of the file
     * @return true if successful! False if file not found.
     */

    public boolean loadDataFromFile(String filepath) {
        try {
            File file = new File(filepath);
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length == 6) {
                    int id = Integer.parseInt(parts[0].trim());
                    String title = parts[1].trim();
                    String platform = parts[2].trim();
                    int year = Integer.parseInt(parts[3].trim());
                    double price = Double.parseDouble(parts[4].trim());
                    boolean multi = Boolean.parseBoolean(parts[5].trim());

                    if (findGameById(id) == null){
                        inventory.add(new VideoGame(id, title, platform, year, price, multi));
                    }
                }
            }
            fileScanner.close();
            return true;
        } catch (FileNotFoundException e){
            return false;
        } catch (Exception e) {
            System.out.println("Warning: Corrupted data line skipped. ");
            return true; //Partially loaded
        }
    }

    /**
     * Adds a new game!
     * @param game Videogame object to add
     * @return true if added, false if ID existed
     */
    public boolean addGame(VideoGame game) {
        if (findGameById(game.getGameID()) != null){
            return false; //Duplicate Id of game found
        }
        inventory.add(game);
        return true;
    }


    /**
     * Removes a game!
     * @param id is game ID to remove
     * @return true if removed, false if not found.
     */
    public boolean removeGame(int id) {
        VideoGame target = findGameById(id);
        if (target != null){
            inventory.remove(target);
            return true;
        }
        return false;
    }


    /**
     * Returns a list of all games
     * @return String of all inventory
     */
    public String displayInventory(){
        if (inventory.isEmpty()){
            return "The inventory is currently empty";
        }
        StringBuilder sb = new StringBuilder();
        for (VideoGame game : inventory){
            sb.append(game.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Custom Action Time! Calculates the total dollar value of the inventory $$$
     * @return double representing the total sum.
     */
    public double calculateTotalValue(){
        double total = 0.0;
        for (VideoGame game : inventory){
            total += game.getPrice();
        }
        return total;
    }

    /**
     * Helper method to find an object by ID
     * @param id the Id
     * @return the VideoGame obj, or null if not found
     */
    public VideoGame findGameById(int id){
        for (VideoGame game : inventory){
            if (game.getGameID() == id){
                return game;
            }
        }
        return null;
    }
}
