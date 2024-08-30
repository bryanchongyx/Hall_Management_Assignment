import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class PageScheduler implements ActionListener {

    JFrame schedulerFrame;
    JButton addHallButton, editHallButton, deleteHallButton, setAvailabilityButton, setMaintenanceButton, logoutButton;
    JTable hallTable;
    DefaultTableModel tableModel;
    JTextField filterField;
    TableRowSorter<DefaultTableModel> rowSorter;

    public PageScheduler() {
        schedulerFrame = new JFrame("Scheduler Dashboard");
        schedulerFrame.setSize(900, 350);
        schedulerFrame.setLocation(500, 325);
        schedulerFrame.setLayout(new BorderLayout());

        // Filter Field Setup
        filterField = new JTextField(15);
        filterField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String text = filterField.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        // Buttons
        addHallButton = new JButton("Add Hall Information");
        editHallButton = new JButton("Edit Hall Information");
        deleteHallButton = new JButton("Delete Hall Information");
        setAvailabilityButton = new JButton("Set Hall Availability");
        setMaintenanceButton = new JButton("Set Hall Maintenance");
        logoutButton = new JButton("Logout");

        addHallButton.addActionListener(this);
        editHallButton.addActionListener(this);
        deleteHallButton.addActionListener(this);
        setAvailabilityButton.addActionListener(this);
        setMaintenanceButton.addActionListener(this);
        logoutButton.addActionListener(this);

        // Table Setup
        String[] columnNames = {"Hall Name", "Hall Type", "Capacity", "Rate per Hour", "Select"};
        tableModel = new DefaultTableModel(columnNames, 0);
        hallTable = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 4 ? Boolean.class : String.class;
            }
        };
        rowSorter = new TableRowSorter<>(tableModel);
        hallTable.setRowSorter(rowSorter);

        loadHallData();

        JScrollPane scrollPane = new JScrollPane(hallTable);

        // Top panel for filter input
        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(filterField);

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(addHallButton);
        bottomPanel.add(editHallButton);
        bottomPanel.add(deleteHallButton);
        bottomPanel.add(setAvailabilityButton);
        bottomPanel.add(setMaintenanceButton);
        bottomPanel.add(logoutButton);

        schedulerFrame.add(filterPanel, BorderLayout.NORTH);
        schedulerFrame.add(scrollPane, BorderLayout.CENTER);
        schedulerFrame.add(bottomPanel, BorderLayout.SOUTH);
        schedulerFrame.setVisible(true);
    }

    // Load hall data into the table
    private void loadHallData() {
        tableModel.setRowCount(0); // Clear existing data
        for (Hall hall : DataIO.allHalls) {
            Object[] rowData = {hall.getHallName(), hall.getHallType(), hall.getCapacity(), hall.getRatePerHour(), false};
            tableModel.addRow(rowData);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == addHallButton) {
                // Add new hall
                String hallName = JOptionPane.showInputDialog("Enter Hall Name:");
                if (hallName == null || hallName.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(schedulerFrame, "Hall Name cannot be empty.");
                    return;
                }

                String[] hallTypes = {"Auditorium", "Banquet Hall", "Meeting Room"};
                String hallType = (String) JOptionPane.showInputDialog(schedulerFrame, "Select Hall Type:", "Hall Type",
                        JOptionPane.QUESTION_MESSAGE, null, hallTypes, hallTypes[0]);

                int capacity = 0;
                double rate = 0.0;

                switch (hallType) {
                    case "Auditorium":
                        capacity = 1000;
                        rate = 300.00;
                        break;
                    case "Banquet Hall":
                        capacity = 300;
                        rate = 100.00;
                        break;
                    case "Meeting Room":
                        capacity = 30;
                        rate = 50.00;
                        break;
                }

                Hall newHall = new Hall(hallName, hallType, capacity, rate);
                DataIO.allHalls.add(newHall);
                DataIO.write(); // Save changes to file

                loadHallData(); // Refresh table data
                JOptionPane.showMessageDialog(schedulerFrame, "Hall added successfully!");

            } else if (e.getSource() == editHallButton) {
                // Edit hall information
                int selectedRow = hallTable.getSelectedRow();
                if (selectedRow != -1) {
                    String currentHallName = (String) hallTable.getValueAt(selectedRow, 0);
                    Hall hallToEdit = DataIO.findHallByName(currentHallName);

                    if (hallToEdit != null) {
                        String newHallName = JOptionPane.showInputDialog("Edit Hall Name:", hallToEdit.getHallName());
                        if (newHallName == null || newHallName.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(schedulerFrame, "Hall Name cannot be empty.");
                            return;
                        }
                        hallToEdit.setHallName(newHallName);

                        String newHallType = (String) JOptionPane.showInputDialog(schedulerFrame, "Select New Hall Type:",
                                "Edit Hall Type", JOptionPane.QUESTION_MESSAGE, null, new String[]{"Auditorium", "Banquet Hall", "Meeting Room"},
                                hallToEdit.getHallType());

                        if (newHallType != null) {
                            hallToEdit.setHallType(newHallType);
                            switch (newHallType) {
                                case "Auditorium":
                                    hallToEdit.setCapacity(1000);
                                    hallToEdit.setRatePerHour(300.00);
                                    break;
                                case "Banquet Hall":
                                    hallToEdit.setCapacity(300);
                                    hallToEdit.setRatePerHour(100.00);
                                    break;
                                case "Meeting Room":
                                    hallToEdit.setCapacity(30);
                                    hallToEdit.setRatePerHour(50.00);
                                    break;
                            }
                            DataIO.write(); // Save changes to file
                            loadHallData(); // Refresh table data
                            JOptionPane.showMessageDialog(schedulerFrame, "Hall information updated successfully!");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(schedulerFrame, "Please select a hall to edit.");
                }

            } else if (e.getSource() == deleteHallButton) {
                // Delete hall
                ArrayList<Hall> hallsToDelete = new ArrayList<>();
                for (int i = 0; i < hallTable.getRowCount(); i++) {
                    boolean isSelected = (boolean) hallTable.getValueAt(i, 4); // Checkbox column
                    if (isSelected) {
                        String hallName = (String) hallTable.getValueAt(i, 0); // Hall name column
                        hallsToDelete.add(DataIO.findHallByName(hallName));
                    }
                }

                if (hallsToDelete.isEmpty()) {
                    JOptionPane.showMessageDialog(schedulerFrame, "No halls selected for deletion.");
                } else {
                    // Remove selected halls from the list and update the file
                    for (Hall hall : hallsToDelete) {
                        DataIO.allHalls.remove(hall);
                    }
                    DataIO.write(); // Save changes to file
                    loadHallData(); // Refresh table data
                    JOptionPane.showMessageDialog(schedulerFrame, "Selected halls deleted successfully.");
                }

            } else if (e.getSource() == setAvailabilityButton) {
                // Set hall availability
                int selectedRow = hallTable.getSelectedRow();
                if (selectedRow != -1) {
                    String hallName = (String) hallTable.getValueAt(selectedRow, 0);
                    Hall hall = DataIO.findHallByName(hallName);

                    if (hall != null) {
                        String startDate = JOptionPane.showInputDialog("Enter start date for availability (YYYY-MM-DD):");
                        if (startDate == null || startDate.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(schedulerFrame, "Start date cannot be empty.");
                            return;
                        }

                        String startTime = JOptionPane.showInputDialog("Enter start time for availability (HH:MM):");
                        if (startTime == null || startTime.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(schedulerFrame, "Start time cannot be empty.");
                            return;
                        }

                        String endDate = JOptionPane.showInputDialog("Enter end date for availability (YYYY-MM-DD):");
                        if (endDate == null || endDate.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(schedulerFrame, "End date cannot be empty.");
                            return;
                        }

                        String endTime = JOptionPane.showInputDialog("Enter end time for availability (HH:MM):");
                        if (endTime == null || endTime.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(schedulerFrame, "End time cannot be empty.");
                            return;
                        }

                        String remarks = JOptionPane.showInputDialog("Enter any remarks (optional):");

                        hall.setAvailability(startDate, startTime, endDate, endTime, remarks);
                        DataIO.write(); // Save changes to file
                        JOptionPane.showMessageDialog(schedulerFrame, "Hall availability set successfully!");
                    } else {
                        JOptionPane.showMessageDialog(schedulerFrame, "Hall not found!");
                    }
                } else {
                    JOptionPane.showMessageDialog(schedulerFrame, "Please select a hall to set availability.");
                }

            } else if (e.getSource() == setMaintenanceButton) {
                // Set hall maintenance schedule
                int selectedRow = hallTable.getSelectedRow();
                if (selectedRow != -1) {
                    String hallName = (String) hallTable.getValueAt(selectedRow, 0);
                    Hall hall = DataIO.findHallByName(hallName);

                    if (hall != null) {
                        String startDate = JOptionPane.showInputDialog("Enter start date for maintenance (YYYY-MM-DD):");
                        if (startDate == null || startDate.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(schedulerFrame, "Start date cannot be empty.");
                            return;
                        }

                        String startTime = JOptionPane.showInputDialog("Enter start time for maintenance (HH:MM):");
                        if (startTime == null || startTime.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(schedulerFrame, "Start time cannot be empty.");
                            return;
                        }

                        String endDate = JOptionPane.showInputDialog("Enter end date for maintenance (YYYY-MM-DD):");
                        if (endDate == null || endDate.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(schedulerFrame, "End date cannot be empty.");
                            return;
                        }

                        String endTime = JOptionPane.showInputDialog("Enter end time for maintenance (HH:MM):");
                        if (endTime == null || endTime.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(schedulerFrame, "End time cannot be empty.");
                            return;
                        }

                        String remarks = JOptionPane.showInputDialog("Enter any remarks (optional):");

                        hall.setMaintenance(startDate, startTime, endDate, endTime, remarks);
                        DataIO.write(); // Save changes to file
                        JOptionPane.showMessageDialog(schedulerFrame, "Hall maintenance schedule set successfully!");
                    } else {
                        JOptionPane.showMessageDialog(schedulerFrame, "Hall not found!");
                    }
                } else {
                    JOptionPane.showMessageDialog(schedulerFrame, "Please select a hall to set maintenance.");
                }

            } else if (e.getSource() == logoutButton) {
                schedulerFrame.setVisible(false);
                Main.a1.a.setVisible(true);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(schedulerFrame, ex.getMessage());
        }
    }
}
