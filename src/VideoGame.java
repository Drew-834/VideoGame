/**
 * Drew Carrillo
 * CEN 3024C
 * 3/08/2026
 * CLASS NAME: VideoGame
 *
 * This class is a data model for our inventory system. It holds the 6 attributes for an entry, using encapsulation and public getters/setters.
 */

public class VideoGame {
    private int gameID;
    private String title;
    private String platform;
    private int releaseYear;
    private double price;
    private boolean isMultiplayer;

    public VideoGame(int gameID, String title, String platform, int releaseYear, double price, boolean isMultiplayer) {
        this.gameID = gameID;
        this.title = title;
        this.platform = platform;
        this.releaseYear = releaseYear;
        this.price = price;
        this.isMultiplayer = isMultiplayer;


    }
    public int getGameID() { return gameID; }
    public String getTitle() { return title; }
    public String getPlatform() { return platform; }
    public int getReleaseYear() { return releaseYear; }
    public double getPrice() { return price; }
    public boolean isMultiplayer() { return isMultiplayer; }

    public void setGameID(int gameID) { this.gameID = gameID; }
    public void setTitle(String title) { this.title = title; }
    public void setPlatform(String platform) { this.platform = platform; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }
    public void setPrice(double price) { this.price = price; }
    public void setIsMultiplayer(boolean isMultiplayer) { this.isMultiplayer = isMultiplayer; }

    /**
     * Formats the object data cleanly
     * @return String containing all the 6 attributes! No void
     */

    @Override
    public String toString() {
        return String.format("ID: %-6d | Title: %-25s | Platform: %-12s | Year: %-4d | Price: $%-6.2f | Multiplayer: %b",
                gameID, title, platform, releaseYear, price, isMultiplayer);
    }
}
