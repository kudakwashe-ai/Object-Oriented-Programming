import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import org.bson.Document;
import org.bson.types.ObjectId;

public class App {
    // UI Theme Colors (Premium Dark Slate Palette)
    private static final Color COLOR_BG = new Color(30, 34, 42);          // Deep slate background
    private static final Color COLOR_PANEL_BG = new Color(40, 44, 52);    // Lighter container background
    private static final Color COLOR_TEXT = new Color(220, 223, 228);      // Soft white text
    private static final Color COLOR_ACCENT = new Color(82, 139, 255);     // Electric blue primary accent
    private static final Color COLOR_ACCENT_HOVER = new Color(110, 160, 255);
    private static final Color COLOR_DANGER = new Color(224, 108, 117);    // Coral red for errors/delete
    private static final Color COLOR_SUCCESS = new Color(152, 195, 121);   // Green for status/add
    private static final Color COLOR_INPUT_BG = new Color(53, 57, 69);     // Dark input field background
    private static final Color COLOR_BORDER = new Color(75, 82, 99);       // Subtle separators

    private final DefaultTableModel tableModel;
    private final JTextField nameField;
    private final JTextField emailField;
    private final JTextField phoneField;
    private final JTextField uriField;
    private JTable table;
    private JLabel statusLabel;

    // MongoDB Fields
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private final List<ObjectId> contactIds = new ArrayList<>();
    private String currentUri = "mongodb://localhost:27017";

    public App() {
        // Apply System Anti-Aliasing for Beautiful Typography
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        tableModel = new DefaultTableModel(
                new String[]{"Name", "Email", "Phone"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable direct cell editing to preserve consistency
            }
        };

        nameField = createStyledTextField(20);
        emailField = createStyledTextField(20);
        phoneField = createStyledTextField(20);
        uriField = createStyledTextField(25);
        uriField.setText(currentUri);

        JFrame frame = new JFrame("Apex Contacts - MongoDB Edition");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(COLOR_BG);
        frame.setLayout(new BorderLayout(15, 15));

        // Create Main Container Panel with EmptyBorder for padding
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(COLOR_BG);
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Top Section: Connection Management + Input Form
        JPanel topContainer = new JPanel(new BorderLayout(10, 10));
        topContainer.setBackground(COLOR_BG);
        topContainer.add(createConnectionPanel(), BorderLayout.NORTH);
        topContainer.add(createFormPanel(), BorderLayout.CENTER);

        contentPanel.add(topContainer, BorderLayout.NORTH);
        contentPanel.add(createTablePanel(), BorderLayout.CENTER);
        contentPanel.add(createStatusPanel(), BorderLayout.SOUTH);

        frame.add(contentPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Attempt initial database connection asynchronously
        SwingUtilities.invokeLater(this::connectToDatabase);
    }

    private JPanel createConnectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_PANEL_BG);
        panel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(COLOR_BORDER, 1),
                "Database Connection Settings",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                COLOR_ACCENT
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel uriLabel = new JLabel("MongoDB URI:");
        uriLabel.setForeground(COLOR_TEXT);
        uriLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(uriLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(uriField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.0;
        JButton connectBtn = createStyledButton("Connect", COLOR_ACCENT);
        connectBtn.addActionListener(e -> {
            currentUri = uriField.getText().trim();
            connectToDatabase();
        });
        panel.add(connectBtn, gbc);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_PANEL_BG);
        panel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(COLOR_BORDER, 1),
                "Contact Information Details",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                COLOR_ACCENT
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 12, 6, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(COLOR_TEXT);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(nameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(COLOR_TEXT);
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(emailField, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setForeground(COLOR_TEXT);
        phoneLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(phoneField, gbc);

        // Action Buttons Row
        JButton addButton = createStyledButton("Add Contact", COLOR_SUCCESS);
        addButton.addActionListener(e -> addContact());

        JButton editButton = createStyledButton("Save Edits", COLOR_ACCENT);
        editButton.addActionListener(e -> editContact());

        JButton deleteButton = createStyledButton("Delete Contact", COLOR_DANGER);
        deleteButton.addActionListener(e -> deleteContact());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setBackground(COLOR_PANEL_BG);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;
        gbc.insets = new Insets(12, 12, 8, 12);
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JScrollPane createTablePanel() {
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setBackground(COLOR_PANEL_BG);
        table.setForeground(COLOR_TEXT);
        table.setGridColor(COLOR_BORDER);
        table.setSelectionBackground(COLOR_ACCENT);
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Format Table Header
        JTableHeader header = table.getTableHeader();
        header.setBackground(COLOR_BG);
        header.setForeground(COLOR_ACCENT);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setReorderingAllowed(false);
        header.setBorder(new LineBorder(COLOR_BORDER));

        // Custom Cell Renderer to match Dark Theme padding & colors
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? COLOR_PANEL_BG : COLOR_BG);
                    c.setForeground(COLOR_TEXT);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Indent cell text
                return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                nameField.setText(tableModel.getValueAt(row, 0).toString());
                emailField.setText(tableModel.getValueAt(row, 1).toString());
                phoneField.setText(tableModel.getValueAt(row, 2).toString());
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(COLOR_BG);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(COLOR_BORDER, 1),
                "Registered Contacts Listing",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                COLOR_ACCENT
        ));
        scrollPane.setPreferredSize(new Dimension(550, 220));
        return scrollPane;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PANEL_BG);
        panel.setBorder(new EmptyBorder(5, 10, 5, 10));

        statusLabel = new JLabel("Initializing Database...");
        statusLabel.setForeground(COLOR_TEXT);
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        panel.add(statusLabel, BorderLayout.WEST);

        return panel;
    }

    // --- Helper UI Styling Creators ---

    private JTextField createStyledTextField(int cols) {
        JTextField field = new JTextField(cols);
        field.setBackground(COLOR_INPUT_BG);
        field.setForeground(COLOR_TEXT);
        field.setCaretColor(COLOR_TEXT);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        return field;
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover Effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });
        return button;
    }

    // --- Database Operations ---

    private void connectToDatabase() {
        statusLabel.setText("Connecting to database: " + currentUri + " ...");
        statusLabel.setForeground(COLOR_TEXT);

        // Perform connection in background thread to avoid freezing Swing UI
        new Thread(() -> {
            try {
                if (mongoClient != null) {
                    mongoClient.close();
                }
                mongoClient = MongoClients.create(currentUri);
                database = mongoClient.getDatabase("contacts_db");
                collection = database.getCollection("contacts");

                // Test connection
                database.runCommand(new Document("ping", 1));

                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Connected successfully to MongoDB.");
                    statusLabel.setForeground(COLOR_SUCCESS);
                    loadContacts();
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Connection Failed: " + e.getMessage());
                    statusLabel.setForeground(COLOR_DANGER);
                    JOptionPane.showMessageDialog(
                            null,
                            "Failed to connect to MongoDB:\n" + e.getMessage(),
                            "Database Connection Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                });
            }
        }).start();
    }

    private void loadContacts() {
        if (collection == null) return;

        new Thread(() -> {
            try {
                List<Document> documents = collection.find().into(new ArrayList<>());
                SwingUtilities.invokeLater(() -> {
                    tableModel.setRowCount(0);
                    contactIds.clear();
                    for (Document doc : documents) {
                        ObjectId id = doc.getObjectId("_id");
                        String name = doc.getString("name");
                        String email = doc.getString("email");
                        String phone = doc.getString("phone");

                        tableModel.addRow(new Object[]{name, email, phone});
                        contactIds.add(id);
                    }
                    clearFields();
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "Error loading contacts: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void addContact() {
        if (collection == null) {
            showNoDbWarning();
            return;
        }

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in Name, Email, and Phone.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        new Thread(() -> {
            try {
                Document doc = new Document("name", name)
                        .append("email", email)
                        .append("phone", phone);
                collection.insertOne(doc);
                ObjectId id = doc.getObjectId("_id");

                SwingUtilities.invokeLater(() -> {
                    tableModel.addRow(new Object[]{name, email, phone});
                    contactIds.add(id);
                    clearFields();
                    nameField.requestFocus();
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "Failed to insert contact: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void editContact() {
        if (collection == null) {
            showNoDbWarning();
            return;
        }

        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a contact to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in Name, Email, and Phone.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ObjectId id = contactIds.get(selectedRow);

        new Thread(() -> {
            try {
                collection.replaceOne(eq("_id", id), new Document("name", name).append("email", email).append("phone", phone));
                SwingUtilities.invokeLater(() -> {
                    tableModel.setValueAt(name, selectedRow, 0);
                    tableModel.setValueAt(email, selectedRow, 1);
                    tableModel.setValueAt(phone, selectedRow, 2);
                    table.clearSelection();
                    clearFields();
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "Failed to update contact: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void deleteContact() {
        if (collection == null) {
            showNoDbWarning();
            return;
        }

        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a contact to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ObjectId id = contactIds.get(selectedRow);

        new Thread(() -> {
            try {
                collection.deleteOne(eq("_id", id));
                SwingUtilities.invokeLater(() -> {
                    tableModel.removeRow(selectedRow);
                    contactIds.remove(selectedRow);
                    table.clearSelection();
                    clearFields();
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "Failed to delete contact: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
    }

    private void showNoDbWarning() {
        JOptionPane.showMessageDialog(
                null,
                "No active MongoDB connection. Please input a valid URI and connect first.",
                "Database Connection Missing",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }
}
