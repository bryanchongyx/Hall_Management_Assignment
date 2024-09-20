import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class PageAdminManagement implements ActionListener {
    JFrame a;
    JButton create, delete, back;
    JTable adminTable;
    DefaultTableModel tableModel;
    PageSuperAdmin superadminPage;

    public PageAdminManagement(PageSuperAdmin superadminPage) {
        this.superadminPage = superadminPage;
        a = new JFrame("Create & Delete Administrator");
        a.setSize(600, 400);
        a.setLocation(500, 325);
        a.setLayout(new BorderLayout(10, 10)); // Adding padding

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel headerLabel = new JLabel("Administrator Management", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(0x3F51B5));
        headerLabel.setPreferredSize(new Dimension(600, 50));
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        // Table Setup
        String[] columnNames = {"Name", "User ID", "Joined Date", "Select"};
        tableModel = new DefaultTableModel(columnNames, 0);
        adminTable = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 3 ? Boolean.class : String.class;
            }
        };
        adminTable.setRowHeight(25); // Adjust row height
        adminTable.setFont(new Font("Arial", Font.PLAIN, 14));
        adminTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        adminTable.setShowVerticalLines(false);
        adminTable.setShowHorizontalLines(true);
        adminTable.setGridColor(new Color(0xE0E0E0));
        loadAdminData();

        JScrollPane scrollPane = new JScrollPane(adminTable);

        // Bottom Panel for Buttons
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        create = createStyledButton("Create");
        delete = createStyledButton("Delete");
        back = createStyledButton("Back");

        bottomPanel.add(create, gbc);
        gbc.gridx++;
        bottomPanel.add(delete, gbc);
        gbc.gridx++;
        bottomPanel.add(back, gbc);

        // Action Listeners
        create.addActionListener(this);
        delete.addActionListener(this);
        back.addActionListener(this);

        // Adding Panels to Frame
        a.add(headerPanel, BorderLayout.NORTH);
        a.add(scrollPane, BorderLayout.CENTER);
        a.add(bottomPanel, BorderLayout.SOUTH);
        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Helper method to create styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(0x4CAF50));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(100, 35));
        button.setToolTipText(text + " Administrator");
        return button;
    }

    public void showPage() {
        a.setVisible(true); // Make the frame visible
    }

    // Load admin data into the table
    private void loadAdminData() {
        tableModel.setRowCount(0); // Clear existing data
        for (Admin admin : DataIO.allAdmin) {
            Object[] rowData = {admin.getFullname(), admin.getUserid(), admin.getDate(), false};
            tableModel.addRow(rowData);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == create) {
                createNewAdmin();
            } else if (e.getSource() == delete) {
                deleteSelectedAdmins();
            } else if (e.getSource() == back) {
                a.setVisible(false);
                Main.a4.a.setVisible(true);;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(a, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to create a new admin
    private void createNewAdmin() throws Exception {
        String admin_fullname = JOptionPane.showInputDialog(a, "Enter Administrator Full Name:").trim();
        if (admin_fullname == null || admin_fullname.isEmpty()) {
            JOptionPane.showMessageDialog(a, "Full Name cannot be empty!", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String admin_userid = JOptionPane.showInputDialog(a, "Enter User ID for new administrator:").trim();
        if (admin_userid == null || admin_userid.isEmpty()) {
            JOptionPane.showMessageDialog(a, "User ID cannot be empty!", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (DataIO.checkUserid(admin_userid) != null) {
            throw new Exception("Admin User ID is taken!");
        }

        String admin_password = JOptionPane.showInputDialog(a, "Set Password for new administrator:").trim();
        if (admin_password == null || admin_password.isEmpty()) {
            JOptionPane.showMessageDialog(a, "Password cannot be empty!", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String todayDate = java.time.LocalDate.now().toString();
        DataIO.allUser.add(new User(admin_fullname, admin_userid, admin_password, "administrator"));
        DataIO.allAdmin.add(new Admin(admin_fullname, admin_userid, admin_password, todayDate));
        DataIO.write(); // Save changes to files
        loadAdminData(); // Refresh table data
        JOptionPane.showMessageDialog(a, "Administrator created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to delete selected admins
    private void deleteSelectedAdmins() throws Exception {
        ArrayList<Admin> adminsToDelete = new ArrayList<>();
        for (int i = 0; i < adminTable.getRowCount(); i++) {
            boolean isSelected = (boolean) adminTable.getValueAt(i, 3); // Checkbox column
            if (isSelected) {
                String userid = (String) adminTable.getValueAt(i, 1); // User ID column
                adminsToDelete.add(DataIO.findAdminByUserId(userid));
            }
        }

        if (adminsToDelete.isEmpty()) {
            JOptionPane.showMessageDialog(a, "No administrators selected for deletion.", "Delete Error", JOptionPane.WARNING_MESSAGE);
        } else {
            int confirm = JOptionPane.showConfirmDialog(a, "Are you sure you want to delete the selected administrators?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                for (Admin admin : adminsToDelete) {
                    DataIO.allAdmin.remove(admin);
                    DataIO.removeUserByUserId(admin.getUserid());
                }
                DataIO.write(); // Save changes to files
                loadAdminData(); // Refresh table data
                JOptionPane.showMessageDialog(a, "Selected administrators deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
