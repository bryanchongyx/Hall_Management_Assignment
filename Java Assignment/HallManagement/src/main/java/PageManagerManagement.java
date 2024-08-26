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
        a.setSize(700, 350);
        a.setLocation(500, 325);
        a.setLayout(new BorderLayout());

        // Buttons
        add = new JButton("Add");
        edit = new JButton("Edit");
        delete = new JButton("Delete");
        logout = new JButton("Logout");
        back = new JButton("Back");

        add.addActionListener(this);
        edit.addActionListener(this);
        delete.addActionListener(this);
        back.addActionListener(this);
        logout.addActionListener(this);

        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Table Setup
        String[] columnNames = {"Full Name", "Username", "Joined Date", "Select"};
        tableModel = new DefaultTableModel(columnNames, 0);
        managerTable = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 3 ? Boolean.class : String.class;
            }
        };
        rowSorter = new TableRowSorter<>(tableModel);
        managerTable.setRowSorter(rowSorter);
        loadManagerData();

        JScrollPane scrollPane = new JScrollPane(managerTable);

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.add(new JLabel("Search:"));
        filterField = new JTextField(20);
        filterPanel.add(filterField);

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

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
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
            Object[] rowData = {manager.getFullname(), manager.getUserid(), manager.getJoinedDate(), false};
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
                // Add new manager staff
                String fullname = JOptionPane.showInputDialog("Enter Manager Full Name:").trim();
                String username = JOptionPane.showInputDialog("Enter Manager Username:").trim();

                if (DataIO.checkUserid(username) != null) {
                    throw new Exception("Username is already taken!");
                }

                String password = JOptionPane.showInputDialog("Set Password for new manager:").trim();
                String todayDate = java.time.LocalDate.now().toString();

                DataIO.allUser.add(new User(fullname, username, password, "manager"));
                DataIO.allManager.add(new Manager(fullname, username, password, todayDate));
                DataIO.write(); // Save changes to files

                loadManagerData(); // Refresh table data

            } else if (e.getSource() == edit) {
                // Edit selected manager information
                int selectedRow = managerTable.getSelectedRow();
                if (selectedRow != -1) {
                    String currentUsername = (String) managerTable.getValueAt(selectedRow, 1);
                    Manager managerToEdit = DataIO.findManagerByUserid(currentUsername);

                    if (managerToEdit != null) {
                        String newFullName = JOptionPane.showInputDialog("Edit Full Name:", managerToEdit.getFullname()).trim();
                        if (newFullName != null && !newFullName.trim().isEmpty()) {
                            managerToEdit.setFullname(newFullName);
                        }
                        
                        String newUsername = JOptionPane.showInputDialog("Edit Username:", managerToEdit.getUserid()).trim();
                        if (newUsername != null && !newUsername.trim().isEmpty() && !newUsername.equals(currentUsername)) {
                            // Update username
                            managerToEdit.setUserid(newUsername);
                            DataIO.updateManagerUserid(currentUsername, newUsername);
                            DataIO.updateUserUserid(currentUsername, newUsername);
                        }
                        

                        String newPassword = JOptionPane.showInputDialog("Edit Password:", managerToEdit.getPassword()).trim();
                        if (newPassword != null && !newPassword.trim().isEmpty()) {
                            managerToEdit.setPassword(newPassword);
                        }

                        DataIO.write(); // Save changes to files
                        loadManagerData(); // Refresh table data
                        JOptionPane.showMessageDialog(a, "Manager information updated successfully.");
                    }
                } else {
                    JOptionPane.showMessageDialog(a, "Please select a manager to edit.");
                }

            } else if (e.getSource() == delete) {
                // Delete selected manager staff
                ArrayList<Manager> managersToDelete = new ArrayList<>();
                for (int i = 0; i < managerTable.getRowCount(); i++) {
                    boolean isSelected = (boolean) managerTable.getValueAt(i, 3); // Checkbox column
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
            }

            else if (e.getSource() == back) {
                a.setVisible(false);
                adminPage.a.setVisible(true);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(a, ex.getMessage());
        }
    }
}
