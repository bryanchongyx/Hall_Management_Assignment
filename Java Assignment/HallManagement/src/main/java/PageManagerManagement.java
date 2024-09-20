import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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

public class PageManagerManagement implements ActionListener {
    JFrame a;
    JButton add, edit, delete, logout, back;
    JTable managerTable;
    DefaultTableModel tableModel;
    JTextField filterField;
    TableRowSorter<DefaultTableModel> rowSorter;
    PageAdmin adminPage; // Reference to the Admin Page

    public PageManagerManagement(PageAdmin adminPage) {
        this.adminPage = adminPage;

        a = new JFrame();
        a.setTitle("Manager Staff Management");
        a.setSize(700, 500);
        a.setLocationRelativeTo(null); // Center the frame
        a.setLayout(new BorderLayout());

        // Buttons
        add = new JButton("Add");
        edit = new JButton("Edit");
        delete = new JButton("Delete");
        logout = new JButton("Logout");
        back = new JButton("Back");

        JButton[] buttons = {add, edit, delete, logout, back};
        for (JButton button : buttons) {
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.setPreferredSize(new Dimension(100, 30));
            button.setBackground(new Color(51, 153, 255)); // Blue color
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.addActionListener(this);
        }

        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Table Setup
        String[] columnNames = {"Full Name", "Username", "Password", "Joined Date", "Select"};
        tableModel = new DefaultTableModel(columnNames, 0);
        managerTable = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 4 ? Boolean.class : String.class;
            }
        };
        rowSorter = new TableRowSorter<>(tableModel);
        managerTable.setRowSorter(rowSorter);
        loadManagerData();

        JScrollPane scrollPane = new JScrollPane(managerTable);

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.LIGHT_GRAY);
        filterPanel.add(new JLabel("Search:"));
        filterField = new JTextField(20);
        filterField.setPreferredSize(new Dimension(200, 30));
        filterField.setFont(new Font("Arial", Font.PLAIN, 14));
        filterPanel.add(filterField);

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

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(Color.GRAY);
        bottomPanel.add(add);
        bottomPanel.add(edit);
        bottomPanel.add(delete);
        bottomPanel.add(back);
        bottomPanel.add(logout);

        a.add(filterPanel, BorderLayout.NORTH);
        a.add(scrollPane, BorderLayout.CENTER);
        a.add(bottomPanel, BorderLayout.SOUTH);
        a.setVisible(true);
    }

    // Load manager data into the table
    private void loadManagerData() {
        tableModel.setRowCount(0); // Clear existing data
        for (Manager manager : DataIO.allManager) {
            Object[] rowData = {manager.getFullname(), manager.getUserid(), manager.getPassword(),
                manager.getJoinedDate(), false};
            tableModel.addRow(rowData);
        }
    }

    public void showPage() {
        a.setVisible(true); // Make the frame visible
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == add) {
                try {
                    // Prompt for Manager Full Name
                    String fullname = JOptionPane.showInputDialog("Enter Manager Full Name:").trim();
                    if (fullname.isEmpty()) {
                        throw new Exception("Full name cannot be empty!");
                    }
            
                    // Prompt for Manager Username
                    String username = JOptionPane.showInputDialog("Enter Manager Username:").trim();
                    if (username.isEmpty()) {
                        throw new Exception("Username cannot be empty!");
                    }
                    if (DataIO.checkUserid(username) != null) {
                        throw new Exception("Username is already taken!");
                    }
            
                    // Prompt for Manager Password
                    String password = JOptionPane.showInputDialog("Set Password for new manager:").trim();
                    if (password.isEmpty()) {
                        throw new Exception("Password cannot be empty!");
                    }
            
                    // Current date as the starting date
                    String todayDate = java.time.LocalDate.now().toString();
            
                    // Add new manager to the system
                    DataIO.allUser.add(new User(fullname, username, password, "manager"));
                    DataIO.allManager.add(new Manager(fullname, username, password, todayDate));
                    DataIO.write(); // Save changes to files
            
                    JOptionPane.showMessageDialog(a, "Manager successfully added");
            
                    loadManagerData(); // Refresh table data
                } catch (Exception ex) {
                    // Display error message if any input is invalid or an exception occurs
                    JOptionPane.showMessageDialog(a, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            else if (e.getSource() == edit) {
                // Get selected rows
                int[] selectedRows = managerTable.getSelectedRows();

                if (selectedRows.length > 1) {
                    // Show a message if no manager or multiple managers are selected
                    JOptionPane.showMessageDialog(a, "Please select exactly one user to edit.");
                } else if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(a, "Please select a user to edit.");
                } else {
                    // Get the selected row index
                    int selectedRow = selectedRows[0];

                    // Get the current UserID of the selected manager
                    String currentUserid = (String) managerTable.getValueAt(selectedRow, 1);

                    // Find the manager by UserID
                    Manager managerToEdit = DataIO.findManagerByUserid(currentUserid);

                    if (managerToEdit != null) {
                        // Prompt to edit Full Name
                        String newFullName = JOptionPane.showInputDialog(a, "Edit Full Name:", managerToEdit.getFullname()).trim();
                        if (newFullName != null && !newFullName.trim().isEmpty()) {
                            managerToEdit.setFullname(newFullName);
                            DataIO.updateManagerFullname(currentUserid, newFullName);
                            DataIO.updateUserFullname(currentUserid, newFullName);
                        }

                        // Prompt to edit User ID
                        String newUserid = JOptionPane.showInputDialog(a, "Edit User ID:", managerToEdit.getUserid()).trim();
                        if (newUserid != null && !newUserid.trim().isEmpty() && !newUserid.equals(currentUserid)) {
                            // Update UserID for both manager and user
                            managerToEdit.setUserid(newUserid);
                            DataIO.updateManagerUserid(currentUserid, newUserid);
                            DataIO.updateUserUserid(currentUserid, newUserid);
                            currentUserid = newUserid; // Update currentUserid reference
                        }

                        // Prompt to edit Password
                        String newPassword = JOptionPane.showInputDialog(a, "Edit Password:", managerToEdit.getPassword()).trim();
                        if (newPassword != null && !newPassword.trim().isEmpty()) {
                            managerToEdit.setPassword(newPassword);
                            DataIO.updateManagerPassword(currentUserid, newPassword); // Use currentUserid which might have been updated
                            DataIO.updateUserPassword(currentUserid, newPassword);
                        }

                        // Save changes to files
                        DataIO.write();
                        // Refresh table data
                        loadManagerData();
                        // Notify the user of the successful update
                        JOptionPane.showMessageDialog(a, "Manager information updated successfully.");
                    }
                }
            } else if (e.getSource() == delete) {
                // Delete selected manager staff
                ArrayList<Manager> managersToDelete = new ArrayList<>();
                for (int i = 0; i < managerTable.getRowCount(); i++) {
                    boolean isSelected = (boolean) managerTable.getValueAt(i, 4); // Checkbox column
                    if (isSelected) {
                        String username = (String) managerTable.getValueAt(i, 1); // Username column
                        managersToDelete.add(DataIO.findManagerByUserid(username));
                    }
                }

                if (managersToDelete.isEmpty()) {
                    JOptionPane.showMessageDialog(a, "No managers selected for deletion.");
                } else {
                    // Remove selected managers from the list and update the file
                    for (Manager manager : managersToDelete) {
                        DataIO.allManager.remove(manager);
                        DataIO.removeUserByUserId(manager.getUserid());
                    }
                    DataIO.write(); // Save changes to files
                    loadManagerData(); // Refresh table data
                    JOptionPane.showMessageDialog(a, "Selected managers deleted successfully.");
                }

            } else if (e.getSource() == logout) {
                a.setVisible(false);
                Main.a1.a.setVisible(true);
            } else if (e.getSource() == back) {
                a.setVisible(false);
                adminPage.a.setVisible(true);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(a, ex.getMessage());
        }
    }
}
