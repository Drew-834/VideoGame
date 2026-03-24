// VideoGame.java - Updated with more comments
/**
 * Drew Carrillo
 * CEN 3024C
 * 03/08/2026
 * CLASS NAME: VideoGame
 *
 * This is a data model for our inventory system.
 * It holds the 6 attributes for each video game entry, using encapsulation
 * (private fields with public getters/setters).
 * Each VideoGame object represents one game in our inventory.
 */
public class VideoGame {
    // Private fields - can only be accessed through getters/setters (encapsulation)
    private int gameID;
    private String title;
    private String platform;
    private int releaseYear;
    private double price;
    private boolean isMultiplayer;

    /**
     * Constructor - creates a new VideoGame object with all required data
     * Includes basic validation to prevent invalid objects from being created
     * @param gameID Unique identifier for the game (must be positive)
     * @param title Name of the game (cannot be empty)
     * @param platform Gaming platform (cannot be empty)
     * @param releaseYear Year the game was released (1950-2026)
     * @param price Current price of the game (non-negative)
     * @param isMultiplayer Whether the game has multiplayer features
     */
    public VideoGame(int gameID, String title, String platform, int releaseYear, double price, boolean isMultiplayer) {
        // Basic validation to ensure we create valid objects
        if (gameID <= 0) {
            throw new IllegalArgumentException("Game ID must be positive");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (platform == null || platform.trim().isEmpty()) {
            throw new IllegalArgumentException("Platform cannot be null or empty");
        }
        if (releaseYear < 1950 || releaseYear > 2026) {
            throw new IllegalArgumentException("Release year must be between 1950 and 2026");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        // Set all the fields
        this.gameID = gameID;
        this.title = title;
        this.platform = platform;
        this.releaseYear = releaseYear;
        this.price = price;
        this.isMultiplayer = isMultiplayer;
    }

    // --- GETTERS ---
    // These methods let other classes read our private fields
    public int getGameID() { return gameID; }
    public String getTitle() { return title; }
    public String getPlatform() { return platform; }
    public int getReleaseYear() { return releaseYear; }
    public double getPrice() { return price; }
    public boolean isMultiplayer() { return isMultiplayer; }

    // --- SETTERS ---
    // These methods let other classes modify our private fields
    public void setGameID(int gameID) { this.gameID = gameID; }
    public void setTitle(String title) { this.title = title; }
    public void setPlatform(String platform) { this.platform = platform; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }
    public void setPrice(double price) { this.price = price; }
    public void setIsMultiplayer(boolean isMultiplayer) { this.isMultiplayer = isMultiplayer; }

    /**
     * Formats the object data as a readable string
     * This is called when we do System.out.println(gameObject)
     */
    @Override
    public String toString() {
        // Format the string with aligned columns for readability
        return String.format("ID: %-6d | Title: %-25s | Platform: %-12s | Year: %-4d | Price: $%-6.2f | Multiplayer: %b",
                gameID, title, platform, releaseYear, price, isMultiplayer);
    }
}