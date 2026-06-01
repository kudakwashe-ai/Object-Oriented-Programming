import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class App {
    private final DefaultTableModel tableModel;
    private final JTextField nameField;
    private final JTextField emailField;
    private final JTextField phoneField;
    private JTable table;

    public App() {
        tableModel = new DefaultTableModel(
                new String[]{"Name", "Email", "Phone"},
                0
        );

        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);

        JFrame frame = new JFrame("Contact Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        frame.add(createFormPanel(), BorderLayout.NORTH);
        frame.add(createTablePanel(), BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Input Contact"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(phoneField, gbc);

        JButton addButton = new JButton("Add to List");
        addButton.addActionListener(e -> addContact());

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> editContact());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteContact());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JScrollPane createTablePanel() {
        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.getTableHeader().setReorderingAllowed(false);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                nameField.setText(tableModel.getValueAt(row, 0).toString());
                emailField.setText(tableModel.getValueAt(row, 1).toString());
                phoneField.setText(tableModel.getValueAt(row, 2).toString());
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Contact List"));
        scrollPane.setPreferredSize(new Dimension(500, 200));
        return scrollPane;
    }

    private void addContact() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Please fill in Name, Email, and Phone.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        tableModel.addRow(new Object[]{name, email, phone});
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        nameField.requestFocus();
    }

    private void editContact() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in Name, Email, and Phone.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            tableModel.setValueAt(name, selectedRow, 0);
            tableModel.setValueAt(email, selectedRow, 1);
            tableModel.setValueAt(phone, selectedRow, 2);
            
            table.clearSelection();
            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");
        } else {
            JOptionPane.showMessageDialog(null, "Please select a contact to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteContact() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");
        } else {
            JOptionPane.showMessageDialog(null, "Please select a contact to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }
}
