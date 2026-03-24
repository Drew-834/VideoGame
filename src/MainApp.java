import java.util.Scanner;

/**
 * Drew Carrillo
 * CEN 3024C
 * 03/11/2026
 * MAINAPP
 * This is the Presentation layer (CLI). Has the main method, instantiates the InventoryManager,
 * and handles all user input with strict validation. Uses switch cases and helper methods for clean code.
 */

public class MainApp {
    private static Scanner scanner = new Scanner(System.in);
    private static InventoryManager manager = new InventoryManager();

    public static void main(String[] args) {
        System.out.println("=== Welcome to the Video Game DMS ===");
        boolean running = true;

        // Main application loop - keeps running until user chooses to exit
        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Load Data from File");
            System.out.println("2. Display All Games");
            System.out.println("3. Add a New Game");
            System.out.println("4. Update an Existing Game");
            System.out.println("5. Remove a Game");
            System.out.println("6. Calculate Total Inventory Value");
            System.out.println("7. Exit\n");

            // Get user's menu choice with validation
            int choice = getValidInt("Enter your choice (1-7): ");

            // Process the user's choice
            switch (choice) {
                case 1:
                    handleLoadFile();
                    break;
                case 2:
                    System.out.println("\n" + manager.displayInventory());
                    break;
                case 3:
                    handleAddGame();
                    break;
                case 4:
                    handleUpdateGame();
                    break;
                case 5:
                    handleRemoveGame();
                    break;
                case 6:
                    double total = manager.calculateTotalValue();
                    System.out.printf("\nTotal Inventory Value: $%.2f\n", total);
                    break;
                case 7:
                    System.out.println("Exiting System. Goodbye!");
                    running = false; // This will exit the while loop
                    break;
                default:
                    // This should never happen due to getValidInt validation, but just in case
                    System.out.println("Invalid option. Please select a number between 1 and 7.");
            }
        }
        scanner.close(); // Close the scanner when done
    }

    /**
     * Handles loading data from a text file into the system
     * Asks user for file path and validates it's not empty
     */
    private static void handleLoadFile() {
        System.out.println("Enter the exact file path (like.. sampleData.txt): ");
        String path = scanner.nextLine().trim();

        // Check if user entered an empty file path
        if (path.isEmpty()) {
            System.out.println("Error: File path cannot be empty.");
            return; // Go back to main menu
        }

        // Try to load the file and show appropriate message
        if (manager.loadDataFromFile(path)) {
            System.out.println("Data successfully loaded!");
            System.out.println(manager.displayInventory());
        } else {
            System.out.println("Invalid file path or file not found. Please try again.");
        }
    }

    /**
     * Handles adding a new game to the inventory
     * Checks for duplicate ID FIRST before asking for other data (user feedback improvement)
     */
    private static void handleAddGame() {
        System.out.println("\n-- Add a New Game --");

        // Step 1: Get game ID and check for duplicates IMMEDIATELY
        // This way user doesn't have to enter all fields if ID already exists
        int id = getValidInt("Enter Game ID (numbers only): ");
        if (manager.findGameById(id) != null) {
            System.out.println("Error: A game with ID " + id + " already exists.");
            return; // Return early so user doesn't have to enter all fields
        }

        // Step 2: Get game title (can't be empty)
        System.out.print("Enter Game Title: ");
        String title = scanner.nextLine().trim();
        while (title.isEmpty()) {
            System.out.print("Title cannot be empty. Enter Game Title: ");
            title = scanner.nextLine().trim();
        }

        // Step 3: Get game platform (can't be empty)
        System.out.print("Enter Game Platform: ");
        String platform = scanner.nextLine().trim();
        while (platform.isEmpty()) {
            System.out.print("Platform cannot be empty. Enter Game Platform: ");
            platform = scanner.nextLine().trim();
        }

        // Step 4: Get release year with validation (1950-2026)
        int year = getValidYear("Enter Release Year (1950-2026): ");

        // Step 5: Get price (fixed: added colon for consistency)
        double price = getValidDouble("Enter Price (like... 59.99): ");

        // Step 6: Get multiplayer status
        boolean isMulti = getValidBoolean("Is it Multiplayer? (true/false): ");

        // Create the new game object with all collected data
        VideoGame newGame = new VideoGame(id, title, platform, year, price, isMulti);

        // Add to inventory and show result
        if (manager.addGame(newGame)) {
            System.out.println("Game successfully added!");
            System.out.println(manager.displayInventory());
        } else {
            System.out.println("Error: Failed to add game.");
        }
    }

    /**
     * Handles updating an existing game's information
     * Shows current data first, then lets user choose what to update
     */
    private static void handleUpdateGame() {
        // First, find the game by ID
        int id = getValidInt("Enter the ID of the game you want to update: ");
        VideoGame game = manager.findGameById(id);

        // If game not found, show error and return
        if (game == null) {
            System.out.println("Error: Game ID not found");
            return;
        }

        // Show current game data so user knows what they're updating
        System.out.println("Current Game Data:");
        System.out.println(game.toString());

        // Show update options
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Update Price");
        System.out.println("2. Update Platform");
        System.out.println("3. Update Title");
        System.out.println("4. Update Release Year");
        System.out.println("5. Update Multiplayer Status");
        System.out.println("6. Cancel");

        int updateChoice = getValidInt("Select an option (1-6): ");

        // Process the update choice
        switch (updateChoice) {
            case 1:
                double newPrice = getValidDouble("Enter new price: ");
                game.setPrice(newPrice);
                System.out.println("Price updated successfully!");
                break;
            case 2:
                System.out.print("Enter new platform: ");
                String newPlatform = scanner.nextLine().trim();
                while (newPlatform.isEmpty()) {
                    System.out.print("Platform cannot be empty. Enter new platform: ");
                    newPlatform = scanner.nextLine().trim();
                }
                game.setPlatform(newPlatform);
                System.out.println("Platform updated successfully!");
                break;
            case 3:
                System.out.print("Enter new title: ");
                String newTitle = scanner.nextLine().trim();
                while (newTitle.isEmpty()) {
                    System.out.print("Title cannot be empty. Enter new title: ");
                    newTitle = scanner.nextLine().trim();
                }
                game.setTitle(newTitle);
                System.out.println("Title updated successfully!");
                break;
            case 4:
                int newYear = getValidYear("Enter new release year (1950-2026): ");
                game.setReleaseYear(newYear);
                System.out.println("Release year updated successfully!");
                break;
            case 5:
                boolean newMulti = getValidBoolean("Is it Multiplayer? (true/false): ");
                game.setIsMultiplayer(newMulti);
                System.out.println("Multiplayer status updated successfully!");
                break;
            case 6:
                System.out.println("Update cancelled.");
                return; // Exit the method without showing updated data
            default:
                System.out.println("Invalid choice. Returning to main menu.");
                return;
        }

        // Show the updated game data
        System.out.println("\nUpdated Game Data:");
        System.out.println(game.toString());
    }

    /**
     * Handles removing a game from the inventory
     * Asks for ID and attempts to remove it
     */
    private static void handleRemoveGame() {
        int id = getValidInt("Enter the ID of the game you want to remove: ");
        if (manager.removeGame(id)) {
            System.out.println("Game successfully removed!");
            System.out.println(manager.displayInventory());
        } else {
            System.out.println("Error: Game ID not found.");
        }
    }

    // --- DATA VALIDATION HELPER METHODS ---

    /**
     * Validates that user enters a valid integer
     * Keeps asking until valid input is provided
     */
    private static int getValidInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    /**
     * Validates that user enters a valid year (1950-2026)
     * Uses getValidInt and then checks the range
     */
    private static int getValidYear(String prompt) {
        while (true) {
            int year = getValidInt(prompt);
            // Video games weren't really a thing before 1950, and 2026 is a reasonable upper limit
            if (year >= 1950 && year <= 2026) {
                return year;
            }
            System.out.println("Invalid year. Please enter a year between 1950 and 2026.");
        }
    }

    /**
     * Validates that user enters a valid double (for price)
     * Ensures the price is not negative
     */
    private static double getValidDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value >= 0) { // Price can't be negative
                    return value;
                }
                System.out.println("Invalid price. Please enter a non-negative number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a decimal number.");
            }
        }
    }

    /**
     * Validates that user enters exactly "true" or "false"
     */
    private static boolean getValidBoolean(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("true")) return true;
            if (input.equals("false")) return false;
            System.out.println("Invalid input. Please enter exactly 'true' or 'false'");
        }
    }
}