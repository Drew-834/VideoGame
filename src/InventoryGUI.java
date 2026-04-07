import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel; // Added for the new table
import java.util.ArrayList; // Added to handle lists for the table

public class InventoryGUI extends JFrame {
    //  the Business Logic layer
    private InventoryManager manager = new InventoryManager();

    // Declare GUI Components at the class level
    private JTextField tfId, tfTitle, tfPlatform, tfYear, tfPrice, tfMulti, tfFilePath;
    private JTextArea taDisplay;

    // New variables for the clickable table
    private JTable table;
    private DefaultTableModel tableModel;

    public InventoryGUI() {
        // Retrieve the content-pane from  container JFrame
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout(5, 5));

        // - TOP : The Input Form ---
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));

        formPanel.add(new JLabel("Game ID (Numbers only):"));
        tfId = new JTextField();
        formPanel.add(tfId);

        formPanel.add(new JLabel("Title:"));
        tfTitle = new JTextField();
        formPanel.add(tfTitle);

        formPanel.add(new JLabel("Platform:"));
        tfPlatform = new JTextField();
        formPanel.add(tfPlatform);

        formPanel.add(new JLabel("Release Year (1950-2026):"));
        tfYear = new JTextField();
        formPanel.add(tfYear);

        formPanel.add(new JLabel("Price (e.g., 59.99):"));
        tfPrice = new JTextField();
        formPanel.add(tfPrice);

        formPanel.add(new JLabel("Is Multiplayer? (true/false):"));
        tfMulti = new JTextField();
        formPanel.add(tfMulti);

        // loading the file (Updated label for DB)
        formPanel.add(new JLabel("Database Path (e.g., inventory.db):"));
        tfFilePath = new JTextField();
        formPanel.add(tfFilePath);

        cp.add(formPanel, BorderLayout.NORTH);

        // --- MIDDLE ZONE (CENTER): The Display Area
        // Split this zone to hold both the new table and your original text area
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        // 1. The Clickable Table
        String[] columns = {"ID", "Title", "Platform", "Year", "Price", "Multiplayer"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        centerPanel.add(new JScrollPane(table));

        // Listener to auto-fill text boxes when a row is clicked
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                tfId.setText(tableModel.getValueAt(selectedRow, 0).toString());
                tfTitle.setText(tableModel.getValueAt(selectedRow, 1).toString());
                tfPlatform.setText(tableModel.getValueAt(selectedRow, 2).toString());
                tfYear.setText(tableModel.getValueAt(selectedRow, 3).toString());
                tfPrice.setText(tableModel.getValueAt(selectedRow, 4).toString());
                tfMulti.setText(tableModel.getValueAt(selectedRow, 5).toString());
            }
        });

        // 2. Your original Display Area
        taDisplay = new JTextArea("=== Welcome to the Video Game DMS ===\n\nEnter data above and click a button below.");
        taDisplay.setEditable(false);
        taDisplay.setFont(new Font("Monospaced", Font.PLAIN, 12));
        centerPanel.add(new JScrollPane(taDisplay));

        cp.add(centerPanel, BorderLayout.CENTER);

        //  BOTTOM: The Buttons ---
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton btnLoad = new JButton("Load DB");
        JButton btnDisplay = new JButton("Display All");
        JButton btnAdd = new JButton("Add Game");
        JButton btnUpdate = new JButton("Update Game");
        JButton btnRemove = new JButton("Remove Game");
        JButton btnTotal = new JButton("Calculate Total");

        buttonPanel.add(btnLoad);
        buttonPanel.add(btnDisplay);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnRemove);
        buttonPanel.add(btnTotal);

        cp.add(buttonPanel, BorderLayout.SOUTH);

        // ---! EVENT LISTENERS ---!!

        // 1. Load File Listener (Refactored to connectToDatabase)
        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String path = tfFilePath.getText().trim();
                if (path.isEmpty()) {
                    taDisplay.setText("Error: Please enter a file name in the 'Database Path' box.");
                } else {
                    if (manager.connectToDatabase(path)) {
                        taDisplay.setText("Database Connected Successfully!\n\n" + manager.displayInventory());
                        tfFilePath.setText("");
                        refreshTable(); // Update visual table
                    } else {
                        taDisplay.setText("Error: Database not found or invalid path.");
                    }
                }
            }
        });

        // 2. Display All Listener
        btnDisplay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                taDisplay.setText("Current Inventory:\n\n" + manager.displayInventory());
                refreshTable(); // Update visual table
            }
        });

        // 3. Add Game Listener
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    int id = Integer.parseInt(tfId.getText().trim());
                    String title = tfTitle.getText().trim();
                    String platform = tfPlatform.getText().trim();
                    int year = Integer.parseInt(tfYear.getText().trim());
                    double price = Double.parseDouble(tfPrice.getText().trim());
                    boolean isMulti = Boolean.parseBoolean(tfMulti.getText().trim());

                    VideoGame newGame = new VideoGame(id, title, platform, year, price, isMulti);
                    if (manager.addGame(newGame)) {
                        taDisplay.setText("Game Added!\n\n" + manager.displayInventory());
                        clearForm();
                        refreshTable(); // Update visual table
                    } else {
                        taDisplay.setText("Error: Game with that ID already exists in DB.");
                    }
                } catch (Exception e) {
                    taDisplay.setText("Error: Invalid input. Please check your form fields.");
                }
            }
        });

        // 4. Update Game Listener (Refactored to use the safer updateGame method)
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    int id = Integer.parseInt(tfId.getText().trim());
                    if (manager.findGameById(id) != null) {
                        String title = tfTitle.getText().trim();
                        String platform = tfPlatform.getText().trim();
                        int year = Integer.parseInt(tfYear.getText().trim());
                        double price = Double.parseDouble(tfPrice.getText().trim());
                        boolean isMulti = Boolean.parseBoolean(tfMulti.getText().trim());

                        // Safely updates the SQL row directly
                        manager.updateGame(new VideoGame(id, title, platform, year, price, isMulti));

                        taDisplay.setText("Game Updated!\n\n" + manager.displayInventory());
                        clearForm();
                        refreshTable(); // Update visual table
                    } else {
                        taDisplay.setText("Error: Game ID not found. Cannot update.");
                    }
                } catch (Exception e) {
                    taDisplay.setText("Error: Please fill out ALL fields correctly to update an existing game.");
                }
            }
        });

        // 5. Remove Game Listener
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    int id = Integer.parseInt(tfId.getText().trim());
                    if (manager.removeGame(id)) {
                        taDisplay.setText("Game Removed!\n\n" + manager.displayInventory());
                        clearForm();
                        refreshTable(); // Update visual table
                    } else {
                        taDisplay.setText("Error: Game ID not found.");
                    }
                } catch (Exception e) {
                    taDisplay.setText("Error: Please enter a valid numerical ID in the Game ID field to remove.");
                }
            }
        });

        // 6. Calculate Total Listener
        btnTotal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                double total = manager.calculateTotalValue();
                taDisplay.setText("Total Inventory Value: $" + String.format("%.2f", total) +
                        "\n\n" + manager.displayInventory());
            }
        });

        // window setup
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit the program when closed
        setTitle("Inventory Manager (Video Game GUI project)");
        setSize(800, 650); // Made slightly taller for the split screen
        setVisible(true); // Show the window
    }

    /**
     * Novice-friendly method to pull fresh data into the new table
     */
    private void refreshTable() {
        tableModel.setRowCount(0); // Wipe visual table clean
        ArrayList<VideoGame> games = manager.getAllGames();
        for (VideoGame g : games) {
            tableModel.addRow(new Object[]{
                    g.getGameID(), g.getTitle(), g.getPlatform(),
                    g.getReleaseYear(), g.getPrice(), g.isMultiplayer()
            });
        }
    }

    /**
     * Helper method just to wipe the text fields clean after a successful action!
     */
    private void clearForm() {
        tfId.setText("");
        tfTitle.setText("");
        tfPlatform.setText("");
        tfYear.setText("");
        tfPrice.setText("");
        tfMulti.setText("");
        table.clearSelection(); // Un-clicks the table
    }

    // Main entry point
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InventoryGUI();
            }
        });
    }
}