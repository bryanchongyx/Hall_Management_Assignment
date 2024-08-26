import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class PageSuperAdmin implements ActionListener {
    JFrame a;
    JButton create, delete, logout;
    JTable adminTable;
    DefaultTableModel tableModel;
    
    public PageSuperAdmin() {
        a = new JFrame();
        a.setTitle("Create & Delete Administrator");
        a.setSize(600, 300);
        a.setLocation(500, 325);
        a.setLayout(new BorderLayout());

        // Buttons
        create = new JButton("Create");
        delete = new JButton("Delete");
        logout = new JButton("Logout");
        
        create.addActionListener(this);
        delete.addActionListener(this);
        logout.addActionListener(this);
        
        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Table Setup
        String[] columnNames = {"Name", "User ID", "Joined Date", "Select"};
        tableModel = new DefaultTableModel(columnNames, 0);
        adminTable = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 3 ? Boolean.class : String.class;
            }
        };
        loadAdminData();

        JScrollPane scrollPane = new JScrollPane(adminTable);

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(create);
        bottomPanel.add(delete);
        bottomPanel.add(logout);

        a.add(scrollPane, BorderLayout.CENTER);
        a.add(bottomPanel, BorderLayout.SOUTH);
        a.setVisible(true);
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
                // Create new administrator
                String admin_fullname = JOptionPane.showInputDialog("Enter Administrator Full Name:").trim();
                String admin_userid = JOptionPane.showInputDialog("Enter User ID for new administrator:").trim();

                if (DataIO.checkUserid(admin_userid) != null) {
                    throw new Exception("Admin User ID is taken!");
                }

                String admin_password = JOptionPane.showInputDialog("Set Password for new administrator:").trim();
                String todayDate = java.time.LocalDate.now().toString();
                

                DataIO.allUser.add(new User(admin_fullname,admin_userid, admin_password, "administrator"));
                DataIO.allAdmin.add(new Admin(admin_fullname, admin_userid, admin_password, todayDate));
                DataIO.write(); // Save changes to files

                loadAdminData(); // Refresh table data

            } else if (e.getSource() == delete) {
                // Delete selected administrators
                ArrayList<Admin> adminsToDelete = new ArrayList<>();
                for (int i = 0; i < adminTable.getRowCount(); i++) {
                    boolean isSelected = (boolean) adminTable.getValueAt(i, 3); // Checkbox column
                    if (isSelected) {
                        String userid = (String) adminTable.getValueAt(i, 1); // User ID column
                        adminsToDelete.add(DataIO.findAdminByUserId(userid));
                    }
                }

                if (adminsToDelete.isEmpty()) {
                    JOptionPane.showMessageDialog(a, "No administrators selected for deletion.");
                } else {
                    // Remove selected admins from the list and update the file
                    for (Admin admin : adminsToDelete) {
                        DataIO.allAdmin.remove(admin);
                        DataIO.removeUserByUserId(admin.getUserid());
                    }
                    DataIO.write(); // Save changes to files
                    loadAdminData(); // Refresh table data
                    JOptionPane.showMessageDialog(a, "Selected administrators deleted successfully.");
                }
            }
            
            else if (e.getSource()== logout){
                a.setVisible(false);
                Main.a1.a.setVisible (true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(a, ex.getMessage());
        }
    }
}
