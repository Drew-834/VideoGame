//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import java.util.Scanner;

/**
 * Drew Carrillo
 * CEN 3024C
 * 03/11/2026
 * MAINAPP
 * This is the Presentation layer (CLI). Has the main method, instantiates the InventoryManager, and handles all user input with strict validation.
 * Like with switch cases and a helper method.
 */

public class MainApp {
    private static Scanner scanner = new Scanner(System.in);
    private static InventoryManager manager = new InventoryManager();

    public static void main(String[] args) {
        System.out.println("=== Welcome to the Video Game DMS ===");
        boolean running = true;

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Load Data from File");
            System.out.println("2. Display All Games");
            System.out.println("3. Add a New Game");
            System.out.println("4. Update an Existing Game");
            System.out.println("5. Remove a Game");
            System.out.println("6. Calculate Total Inventory Value");
            System.out.println("7. Exit\n");

            int choice = getValidInt("Enter your choice (1-7) ");

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
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please select a number between 1 and 7.");
            }
        }
        scanner.close();
    }

    // --- UI HELPER METHODS FOR VALIDATION, ETC ---
    private static void handleLoadFile() {
        System.out.println("Enter the exact file path (like.. sampleData.txt");
        String path = scanner.nextLine().trim();

        if (manager.loadDataFromFile(path)) {
            System.out.println("Data successfully loaded!.");
            System.out.println(manager.displayInventory());
        } else {
            System.out.println("Invalid file path. Please try again.");
        }
    }
    private static void handleAddGame() {
        System.out.println("/n-- Add a New Game --");
        int id = getValidInt("Enter Game ID (numbers only): ");

        System.out.println("Enter Game Title: ");
        String title = scanner.nextLine();

        System.out.println("Enter Game Platform: ");
        String platform = scanner.nextLine();

        int year = getValidInt("Enter Release Year (like... 2004): ");
        double price = getValidDouble("Enter Price (like... 59.99");
        boolean isMulti = getValidBoolean("Is it Multiplayer? (true/false): ");

        VideoGame newGame = new VideoGame(id, title, platform, year, price, isMulti);

        if (manager.addGame(newGame)) {
            System.out.println("Game successfully added!");
            System.out.println(manager.displayInventory());
        } else {
            System.out.println("Error: A game with ID " + id + " already exists.");
        }
    }

    private static void handleUpdateGame() {
        int id = getValidInt("Enter the ID of the game you want to update: ");
        VideoGame game = manager.findGameById(id);

        if (game == null) {
            System.out.println("Error: Game ID not found");
            return;
        }
        System.out.println("Updating: " + game.getTitle());
        System.out.println("1. Update Price");
        System.out.println("3. Update Platform");
        int updateChoice = getValidInt("What would you like to update? (1-2): ");

        if (updateChoice == 1) {
            double newPrice = getValidDouble("Enter new price: ");
            game.setPrice(newPrice);
            System.out.println("Price updated successfully!");
        } else if (updateChoice == 2) {
            System.out.print("Enter new platform: ");
            String newPlatform = scanner.nextLine();
            game.setPlatform(newPlatform);
            System.out.println("Platform updated successfully!");
        } else {
            System.out.println("Invalid choice. Returning to main menu.");
        }
        System.out.println(manager.displayInventory());
    }

    private static void handleRemoveGame() {
        int id = getValidInt("Enter the ID of the game you want to remove: ");
        if  (manager.removeGame(id)) {
            System.out.println("Game successfully removed!");
            System.out.println(manager.displayInventory());
        } else  {
            System.out.println("Error: Game ID not found.");
        }
    }

    // --- DATA VALIDATION TO PREVENT CRASHES ---
    private static int getValidInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            }catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    private static double getValidDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            }catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a decimal number.");
            }
        }
    }

    private static boolean getValidBoolean(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("true")) return true;
            if (input.equals("false")) return false;
            System.out.println("Invalid input. Please enter exactly 'true or 'false'");
        }
    }
}