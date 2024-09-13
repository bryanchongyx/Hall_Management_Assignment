import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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
        a.setSize(700, 350);
        a.setLocation(500, 325);
        a.setLayout(new BorderLayout());

        // Buttons
        deleteButton = new JButton("Delete");
        logoutButton = new JButton("Logout");
        backButton = new JButton("Back");
        filterField = new JTextField(15);

        deleteButton.addActionListener(this);
        logoutButton.addActionListener(this);
        backButton.addActionListener(this);

        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Table Setup
        String[] columnNames = {"Full Name", "Username", "Email", "Phone Number", "Select"};
        tableModel = new DefaultTableModel(columnNames, 0);
        customerTable = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 4 ? Boolean.class : String.class;
            }
        };
        rowSorter = new TableRowSorter<>(tableModel);
        customerTable.setRowSorter(rowSorter);
        loadCustomerData();

        JScrollPane scrollPane = new JScrollPane(customerTable);

        // Top panel for filter
        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(filterField);

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(deleteButton);
        bottomPanel.add(backButton);
        bottomPanel.add(logoutButton);

        a.add(filterPanel, BorderLayout.NORTH);
        a.add(scrollPane, BorderLayout.CENTER);
        a.add(bottomPanel, BorderLayout.SOUTH);
        a.setVisible(true);

        // Add KeyListener for the filterField
        filterField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = filterField.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
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
