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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class PageSchedulerManagement implements ActionListener {
    JFrame a;
    JButton add, edit, delete, logout, back;
    JTable schedulerTable;
    DefaultTableModel tableModel;
    JTextField filterField; // For filtering
    TableRowSorter<DefaultTableModel> rowSorter;
    PageAdmin adminPage;

    public PageSchedulerManagement(PageAdmin adminPage) {
        this.adminPage = adminPage;
        
        a = new JFrame();
        a.setTitle("Scheduler Staff Management");
        a.setSize(600, 350);
        a.setLocation(500, 325);
        a.setLayout(new BorderLayout());

        // Filter Field Setup
        filterField = new JTextField(15);
        filterField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = filterField.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        // Buttons
        add = new JButton("Add");
        edit = new JButton("Edit");
        delete = new JButton("Delete");
        back = new JButton ("Back");
        logout = new JButton("Logout");

        add.addActionListener(this);
        edit.addActionListener(this);
        delete.addActionListener(this);
        logout.addActionListener(this);
        back.addActionListener(this);
        
        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Table Setup
        String[] columnNames = {"Full Name", "Username", "Joined Date", "Select"};
        tableModel = new DefaultTableModel(columnNames, 0);
        schedulerTable = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 3 ? Boolean.class : String.class;
            }
        };
        rowSorter = new TableRowSorter<>(tableModel);
        schedulerTable.setRowSorter(rowSorter);

        loadSchedulerData();

        JScrollPane scrollPane = new JScrollPane(schedulerTable);

        // Top panel for filter input
        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.add(new JLabel ("Search:"));
        filterPanel.add(filterField);

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(add);
        bottomPanel.add(edit);
        bottomPanel.add(delete);
        bottomPanel.add(logout);
        bottomPanel.add (back);

        a.add(filterPanel, BorderLayout.NORTH);
        a.add(scrollPane, BorderLayout.CENTER);
        a.add(bottomPanel, BorderLayout.SOUTH);
        a.setVisible(true);
    }

    // Load scheduler data into the table
    private void loadSchedulerData() {
        tableModel.setRowCount(0); // Clear existing data
        for (Scheduler scheduler : DataIO.allScheduler) {
            Object[] rowData = {scheduler.getFullname(), scheduler.getUserid(), scheduler.getJoinedDate(), false};
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
                // Add new scheduler staff
                String fullname = JOptionPane.showInputDialog("Enter Scheduler Full Name:").trim();
                String username = JOptionPane.showInputDialog("Enter Scheduler Username:").trim();

                if (DataIO.checkUserid(username) != null) {
                    throw new Exception("Username is already taken!");
                }

                String password = JOptionPane.showInputDialog("Set Password for new scheduler:").trim();
                String todayDate = java.time.LocalDate.now().toString();

                DataIO.allUser.add(new User(fullname, username, password, "scheduler"));
                DataIO.allScheduler.add(new Scheduler(fullname, username, password, todayDate));
                DataIO.write(); // Save changes to files

                loadSchedulerData(); // Refresh table data

            } else if (e.getSource() == edit) {
                // Edit selected scheduler information
                int selectedRow = schedulerTable.getSelectedRow();
                if (selectedRow != -1) {
                    String currentUserid = (String) schedulerTable.getValueAt(selectedRow, 1);
                    Scheduler schedulerToEdit = DataIO.findSchedulerByUserid(currentUserid);

                    if (schedulerToEdit != null) {
                        String newFullName = JOptionPane.showInputDialog("Edit Full Name:", schedulerToEdit.getFullname()).trim();
                        if (newFullName != null && !newFullName.trim().isEmpty()) {
                            schedulerToEdit.setFullname(newFullName);
                        }
                        
                        String newUsername = JOptionPane.showInputDialog("Edit Username:", schedulerToEdit.getUserid()).trim();
                        if (newUsername != null && !newUsername.trim().isEmpty() && !newUsername.equals(currentUserid)) {
                            // Update username
                            schedulerToEdit.setUserid(newUsername);
                            DataIO.updateSchedulerUserid(currentUserid, newUsername);
                            DataIO.updateUserUserid(currentUserid, newUsername);
                        }


                        String newPassword = JOptionPane.showInputDialog("Edit Password:", schedulerToEdit.getPassword()).trim();
                        if (newPassword != null && !newPassword.trim().isEmpty()) {
                            schedulerToEdit.setPassword(newPassword);
                        }

                        DataIO.write(); // Save changes to files
                        loadSchedulerData(); // Refresh table data
                        JOptionPane.showMessageDialog(a, "Scheduler information updated successfully.");
                    }
                } else {
                    JOptionPane.showMessageDialog(a, "Please select a scheduler to edit.");
                }

            } else if (e.getSource() == delete) {
                // Delete selected scheduler staff
                ArrayList<Scheduler> schedulersToDelete = new ArrayList<>();
                for (int i = 0; i < schedulerTable.getRowCount(); i++) {
                    boolean isSelected = (boolean) schedulerTable.getValueAt(i, 3); // Checkbox column
                    if (isSelected) {
                        String userid = (String) schedulerTable.getValueAt(i, 1); // Username column
                        schedulersToDelete.add(DataIO.findSchedulerByUserid(userid));
                    }
                }

                if (schedulersToDelete.isEmpty()) {
                    JOptionPane.showMessageDialog(a, "No schedulers selected for deletion.");
                } else {
                    // Remove selected schedulers from the list and update the file
                    for (Scheduler scheduler : schedulersToDelete) {
                        DataIO.allScheduler.remove(scheduler);
                        DataIO.removeUserByUserId(scheduler.getUserid());
                    }
                    DataIO.write(); // Save changes to files
                    loadSchedulerData(); // Refresh table data
                    JOptionPane.showMessageDialog(a, "Selected schedulers deleted successfully.");
                }
            } else if (e.getSource() == logout) {
                a.setVisible(false);
                Main.a1.a.setVisible(true);
            }
            
            else if (e.getSource() == back){
                a.setVisible(false);
                adminPage.a.setVisible(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(a, ex.getMessage());
        }
    }
}
