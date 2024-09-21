import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class PageCustomerManagement implements ActionListener {
    JFrame a;
    JButton deleteButton, logoutButton, backButton;
    JTextField filterField;
    JTable customerTable;
    DefaultTableModel tableModel;
    TableRowSorter<DefaultTableModel> rowSorter;
    PageAdmin adminPage;

    public PageCustomerManagement(PageAdmin adminPage) {
        this.adminPage = adminPage;

        a = new JFrame();
        a.setTitle("Customer Management");
        a.setSize(700, 400);
        a.setLocationRelativeTo(null); // Center the frame
        a.setLayout(new BorderLayout());

        // Buttons
        deleteButton = new JButton("Delete");
        logoutButton = new JButton("Logout");
        backButton = new JButton("Back");
        filterField = new JTextField(15);

        deleteButton.addActionListener(this);
        logoutButton.addActionListener(this);
        backButton.addActionListener(this);

        // Table Setup
        String[] columnNames = {"Full Name", "Username", "Email", "Phone Number", "Select"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 4 ? Boolean.class : String.class;
            }
        };
        customerTable = new JTable(tableModel);
        rowSorter = new TableRowSorter<>(tableModel);
        customerTable.setRowSorter(rowSorter);
        loadCustomerData();

        JScrollPane scrollPane = new JScrollPane(customerTable);

        // Top panel for filter input
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(filterField);

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        bottomPanel.add(deleteButton, gbc);
        gbc.gridx = 1;
        bottomPanel.add(backButton, gbc);
        gbc.gridx = 2;
        bottomPanel.add(logoutButton, gbc);

        // Add panels and scroll pane to the frame
        a.add(filterPanel, BorderLayout.NORTH);
        a.add(scrollPane, BorderLayout.CENTER);
        a.add(bottomPanel, BorderLayout.SOUTH);
        a.setVisible(true);

        // Filter Field Setup
        filterField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = filterField.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    // Custom RowFilter to search across all columns
                    rowSorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
                        @Override
                        public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                            for (int i = 0; i < entry.getValueCount(); i++) {
                                // Check if any column contains the filter text
                                if (entry.getStringValue(i).toLowerCase().contains(text.toLowerCase())) {
                                    return true; // If found, include this row
                                }
                            }
                            return false; // If not found, exclude this row
                        }
                    });
                }
            }
        });
    }

    // Load customer data into the table
    private void loadCustomerData() {
        tableModel.setRowCount(0); // Clear existing data
        for (Customer customer : DataIO.allCustomer) {
            Object[] rowData = {customer.getFullname(), customer.getUserid(), customer.getEmail(), customer.getPh_number(), false};
            tableModel.addRow(rowData);
        }
    }

    public void showPage() {
        a.setVisible(true); // Make the frame visible
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == deleteButton) {
            // Delete selected customers
            ArrayList<Customer> customersToDelete = new ArrayList<>();
            for (int i = 0; i < customerTable.getRowCount(); i++) {
                boolean isSelected = (boolean) customerTable.getValueAt(i, 4); // Checkbox column
                if (isSelected) {
                    String userid = (String) customerTable.getValueAt(i, 1); // Username column
                    customersToDelete.add(DataIO.checkCustomerUserid(userid));
                }
            }

            if (customersToDelete.isEmpty()) {
                JOptionPane.showMessageDialog(a, "No customers selected for deletion.");
            } else {
                // Remove selected customers from the list and update the file
                for (Customer customer : customersToDelete) {
                    DataIO.allCustomer.remove(customer);
                    DataIO.removeUserByUserId(customer.getUserid());
                }
                DataIO.write(); // Save changes to files
                loadCustomerData(); // Refresh table data
                JOptionPane.showMessageDialog(a, "Selected customers deleted successfully.");
            }
        } else if (e.getSource() == logoutButton) {
            a.setVisible(false);
            Main.a1.a.setVisible(true);
        } else if (e.getSource() == backButton) {
            a.setVisible(false);
            adminPage.a.setVisible(true);
        }
    }
}
