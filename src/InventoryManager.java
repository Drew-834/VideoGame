import java.sql.*;
import java.util.ArrayList;

/**
 * Drew Carrillo
 * CEN 3024C
 * 03/10/2026
 * CLASS NAME: InventoryManager
 * * This is the Business Logic layer. It manages the SQLite database connection.
 * It contains all CRUD operations and the custom calculation (away from the UI).
 * Safety checks have been added to ensure the program never crashes if the
 * user clicks a button before connecting to the database.
 */
public class InventoryManager {
    // We replaced the temporary ArrayList with a permanent Database Connection
    private Connection conn;

    /**
     * Constructor is empty now since we don't need to build an ArrayList.
     * The connection remains null until the user loads the database.
     */
    public InventoryManager() {
    }

    /**
     * Connects to the SQLite database file.
     * The rubric requires us to NOT hardcode this so the professor can use their own file!
     * * @param filepath Location/Name of the database file (e.g., "inventory.db")
     * @return true if connected successfully, false if file not found/error
     */
    public boolean connectToDatabase(String filepath) {
        try {
            // jdbc:sqlite: tells Java which database driver to use
            String url = "jdbc:sqlite:" + filepath;
            conn = DriverManager.getConnection(url);
            return true;
        } catch (SQLException e) {
            System.out.println("Warning: Unexpected error connecting to DB: " + e.getMessage());
            return false;
        }
    }

    /**
     * Helper method to grab all games from the database and put them in a list.
     * This replaces reading a text file line-by-line.
     * * @return An ArrayList containing all VideoGame objects currently in the database.
     */
    public ArrayList<VideoGame> getAllGames() {
        ArrayList<VideoGame> inventory = new ArrayList<>();

        // SAFETY CHECK: If the database isn't connected yet, return an empty list instead of crashing!
        if (conn == null) {
            return inventory;
        }

        String sql = "SELECT * FROM games";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Loop through the database results and turn them into Java objects
            while (rs.next()) {
                inventory.add(new VideoGame(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("platform"),
                        rs.getInt("releaseYear"),
                        rs.getDouble("price"),
                        rs.getBoolean("isMultiplayer")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Warning: Error reading database.");
        }
        return inventory;
    }

    /**
     * Adds a newly created game to the SQLite database.
     * * @param game The VideoGame object to insert
     * @return true if successfully added, false if ID already exists or DB error
     */
    public boolean addGame(VideoGame game) {
        // SAFETY CHECK: Ensure DB is connected and prevent duplicate IDs!
        if (conn == null || findGameById(game.getGameID()) != null) {
            return false;
        }

        String sql = "INSERT INTO games(id, title, platform, releaseYear, price, isMultiplayer) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, game.getGameID());
            pstmt.setString(2, game.getTitle());
            pstmt.setString(3, game.getPlatform());
            pstmt.setInt(4, game.getReleaseYear());
            pstmt.setDouble(5, game.getPrice());
            pstmt.setBoolean(6, game.isMultiplayer());
            pstmt.executeUpdate();
            return true; // Successfully added
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Removes a game from the database using its unique ID.
     * * @param id The ID of the game to delete
     * @return true if successfully removed, false if not found
     */
    public boolean removeGame(int id) {
        // SAFETY CHECK
        if (conn == null) return false;

        VideoGame target = findGameById(id);
        if (target != null) {
            String sql = "DELETE FROM games WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                return false;
            }
        }
        return false; // ID not found
    }

    /**
     * Updates an existing game in the database.
     * * @param game The VideoGame object containing the updated information
     * @return true if successful, false if error
     */
    public boolean updateGame(VideoGame game) {
        // SAFETY CHECK
        if (conn == null) return false;

        String sql = "UPDATE games SET title = ?, platform = ?, releaseYear = ?, price = ?, isMultiplayer = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, game.getTitle());
            pstmt.setString(2, game.getPlatform());
            pstmt.setInt(3, game.getReleaseYear());
            pstmt.setDouble(4, game.getPrice());
            pstmt.setBoolean(5, game.isMultiplayer());
            pstmt.setInt(6, game.getGameID());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Returns a formatted string of all games in the inventory for the GUI text area.
     * * @return String containing all game information
     */
    public String displayInventory() {
        // SAFETY CHECK: Warn the user instead of crashing
        if (conn == null) {
            return "Please connect to the database first (Type the DB name and click Load DB).";
        }

        ArrayList<VideoGame> inventory = getAllGames();
        if (inventory.isEmpty()) {
            return "The database is currently empty.";
        }

        StringBuilder sb = new StringBuilder();
        for (VideoGame game : inventory) {
            sb.append(game.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * CUSTOM ACTION: Calculates the total dollar value using a mathematical SQL query.
     * * @return double representing the total sum of all prices
     */
    public double calculateTotalValue() {
        // SAFETY CHECK
        if (conn == null) return 0.0;

        String sql = "SELECT SUM(price) AS total FROM games";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Calculation error.");
        }
        return 0.0;
    }

    /**
     * Helper method to find a specific game by its ID in the database.
     * * @param id The ID to search for
     * @return The VideoGame object if found, null if not found
     */
    public VideoGame findGameById(int id) {
        // SAFETY CHECK
        if (conn == null) return null;

        String sql = "SELECT * FROM games WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new VideoGame(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("platform"),
                        rs.getInt("releaseYear"),
                        rs.getDouble("price"),
                        rs.getBoolean("isMultiplayer")
                );
            }
        } catch (SQLException e) {
            // Error handling
        }
        return null; // Not found
    }
}