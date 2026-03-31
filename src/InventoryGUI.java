import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class InventoryGUI extends JFrame {
    //  the Business Logic layer
    private InventoryManager manager = new InventoryManager();

    // Declare GUI Components at the class level
    private JTextField tfId, tfTitle, tfPlatform, tfYear, tfPrice, tfMulti, tfFilePath;
    private JTextArea taDisplay;

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

        // loading the file
        formPanel.add(new JLabel("File Path (e.g., sampleData.txt):"));
        tfFilePath = new JTextField();
        formPanel.add(tfFilePath);

        cp.add(formPanel, BorderLayout.NORTH);

        // --- MIDDLE ZONE (CENTER): The Display Area
        taDisplay = new JTextArea("=== Welcome to the Video Game DMS ===\n\nEnter data above and click a button below.");
        taDisplay.setEditable(false);
        taDisplay.setFont(new Font("Monospaced", Font.PLAIN, 12));
        cp.add(new JScrollPane(taDisplay), BorderLayout.CENTER);

        //  BOTTOM: The Buttons ---
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton btnLoad = new JButton("Load File");
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

        // 1. Load File Listener
        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String path = tfFilePath.getText().trim(); // Pull directly from the new text field
                if (path.isEmpty()) {
                    taDisplay.setText("Error: Please enter a file name in the 'File Path' box.");
                } else {
                    if (manager.loadDataFromFile(path)) {
                        taDisplay.setText("Data Loaded Successfully!\n\n" + manager.displayInventory());
                        tfFilePath.setText(""); // Clear the path box after loading, nice and clean
                    } else {
                        taDisplay.setText("Error: File not found or invalid path.");
                    }
                }
            }
        });

        // 2. Display All Listener
        btnDisplay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                taDisplay.setText("Current Inventory:\n\n" + manager.displayInventory());
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
                    } else {
                        taDisplay.setText("Error: Game with that ID already exists.");
                    }
                } catch (Exception e) {
                    taDisplay.setText("Error: Invalid input. Please check your form fields.");
                }
            }
        });

        // 4. Update Game Listener
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    int id = Integer.parseInt(tfId.getText().trim());
                    if (manager.findGameById(id) != null) {
                        manager.removeGame(id);

                        String title = tfTitle.getText().trim();
                        String platform = tfPlatform.getText().trim();
                        int year = Integer.parseInt(tfYear.getText().trim());
                        double price = Double.parseDouble(tfPrice.getText().trim());
                        boolean isMulti = Boolean.parseBoolean(tfMulti.getText().trim());

                        manager.addGame(new VideoGame(id, title, platform, year, price, isMulti));

                        taDisplay.setText("Game Updated!\n\n" + manager.displayInventory());
                        clearForm();
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
        setSize(750, 550);
        setVisible(true); // Show the window
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