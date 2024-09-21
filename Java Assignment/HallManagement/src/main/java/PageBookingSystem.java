import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.swing.table.DefaultTableModel;

public class PageBookingSystem extends JFrame {
    private List<Hall> halls;
    private List<Booking> bookings;
    private User currentUser;
    private JTable hallTable;
    private DefaultTableModel tableModel;

    public PageBookingSystem(User user) {
        this.currentUser = user;
        this.halls = DataIO.allHall; // Assuming you load halls from DataIO
        this.bookings = DataIO.allBooking; // Assuming you load bookings from DataIO
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Hall Booking System");
        setSize(600, 400);
        setLocation(525, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Buttons
        JButton bookHallButton = new JButton("Book a Hall");
        bookHallButton.setBounds(50, 50, 150, 30);
        add(bookHallButton);

        JButton viewBookingsButton = new JButton("View Bookings");
        viewBookingsButton.setBounds(50, 100, 150, 30);
        add(viewBookingsButton);

        JButton cancelBookingButton = new JButton("Cancel Booking");
        cancelBookingButton.setBounds(50, 150, 150, 30);
        add(cancelBookingButton);

        JButton raiseIssueButton = new JButton("Raise an Issue");
        raiseIssueButton.setBounds(50, 200, 150, 30);
        add(raiseIssueButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(50, 250, 150, 30);
        add(logoutButton);

        // Action Listeners
        bookHallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBookingUI();
            }
        });

        viewBookingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showViewBookingsUI();
            }
        });

        cancelBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCancelBookingUI();
            }
        });

        raiseIssueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRaiseIssueUI();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Main.a1.setVisible(true);
            }
        });

        setVisible(true);
    }

    private void showBookingUI() {
        JFrame bookingFrame = new JFrame("Select a Hall");
        bookingFrame.setSize(900, 400);
        bookingFrame.setLayout(new BorderLayout());
        bookingFrame.setLocation(600, 300);

        // Customer enters the number of people (pax)
        String paxStr = JOptionPane.showInputDialog("Enter number of people (pax):");
        int pax = Integer.parseInt(paxStr);

        // Determine the hall type based on pax
        String hallType = determineHallType(pax);
        if (hallType == null) {
            JOptionPane.showMessageDialog(this, "Sorry, we do not have a hall that meets your required capacity.");
            return;
        }

        // Customer enters the desired booking date
        String dateStr = JOptionPane.showInputDialog("Enter Date (YYYY-MM-DD):");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date bookingDate;
        try {
            bookingDate = dateFormat.parse(dateStr);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format.");
            return;
        }

        // Create Table to show available halls of assigned type
        String[] columnNames = {"Hall Name", "Hall Type", "Capacity", "Rate per Hour", "Available From", "Available Till", "Select"};
        tableModel = new DefaultTableModel(columnNames, 0);
        hallTable = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 6 ? Boolean.class : String.class;
            }
        };

        loadHallData(hallType, bookingDate);

        JScrollPane scrollPane = new JScrollPane(hallTable);
        bookingFrame.add(scrollPane, BorderLayout.CENTER);

        JButton confirmButton = new JButton("Book Now");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processSelectedHall(bookingDate);
            }
        });

        bookingFrame.add(confirmButton, BorderLayout.SOUTH);
        bookingFrame.setVisible(true);
    }


    private void showCancelBookingUI() {
        String bookingToCancel = JOptionPane.showInputDialog("Enter the date of the booking to cancel (YYYY-MM-DD):");
        boolean bookingFound = false;
    
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);
            String bookingDateStr = dateFormat.format(booking.getBookingDate());
    
            if (bookingDateStr.equals(bookingToCancel)) {
                try {
                    booking.cancelBooking(); // Check if the booking can be canceled
                    bookings.remove(i); // Remove the booking from the list in memory
                    bookingFound = true;
                    JOptionPane.showMessageDialog(this, "Booking for " + bookingToCancel + " has been canceled.");
    
                    // Update the bookings.txt file to reflect this removal
                    DataIO.updateBookings(); // Write the remaining bookings to the file
    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
                break;
            }
        }
    
        if (!bookingFound) {
            JOptionPane.showMessageDialog(this, "No booking found for " + bookingToCancel);
        }
    }

    
    private void showRaiseIssueUI() {
        JFrame issueFrame = new JFrame("Raise an Issue");
        issueFrame.setSize(400, 300);
        issueFrame.setLayout(null);
        issueFrame.setLocation(575, 275);
    
        JButton acIssueButton = new JButton("Fix the Air Condition");
        acIssueButton.setBounds(50, 50, 200, 30);
        issueFrame.add(acIssueButton);
    
        JButton lightsIssueButton = new JButton("Lights Not Working");
        lightsIssueButton.setBounds(50, 100, 200, 30);
        issueFrame.add(lightsIssueButton);
    
        JButton seatsIssueButton = new JButton("Less No of Seats");
        seatsIssueButton.setBounds(50, 150, 200, 30);
        issueFrame.add(seatsIssueButton);
    
        acIssueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleIssue("Fix the Air Condition");
            }
        });
    
        lightsIssueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleIssue("Lights Not Working");
            }
        });
    
        seatsIssueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleIssue("Less No of Seats");
            }
        });
    
        issueFrame.setVisible(true);
    }

    
    private void handleIssue(String issueDescription) {
        // Create the frame for issue reporting
        JFrame issueFrame = new JFrame("Report an Issue");
        issueFrame.setSize(400, 200);
        issueFrame.setLayout(null);
        issueFrame.setLocation(600, 300);
    
        // Label for hall selection
        JLabel hallLabel = new JLabel("Select Your Hall:");
        hallLabel.setBounds(30, 20, 150, 25);
        issueFrame.add(hallLabel);
    
        // Dropdown for selecting booked halls (assumes customer has bookings)
        JComboBox<String> hallDropdown = new JComboBox<>();
        hallDropdown.setBounds(150, 20, 200, 25);
        issueFrame.add(hallDropdown);
    
        // Add the user's booked halls to the dropdown
        for (Booking booking : bookings) {
            if (booking.getUser().getUserid().equals(currentUser.getUserid())) {
                hallDropdown.addItem(booking.getHall().getHallName()); // Add the hall name
            }
        }
    
        // Submit button to handle the feedback submission
        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(150, 110, 100, 30);
        issueFrame.add(submitButton);
    
        // Handle the submission when the user clicks submit
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedHall = (String) hallDropdown.getSelectedItem();
    
                if (selectedHall != null && issueDescription != null) {
                    Booking relatedBooking = null;
    
                    // Find the booking related to the selected hall
                    for (Booking booking : bookings) {
                        if (booking.getUser().getUserid().equals(currentUser.getUserid()) &&
                            booking.getHall().getHallName().equalsIgnoreCase(selectedHall)) {
                            relatedBooking = booking;
                            break;
                        }
                    }
    
                    // Ensure a valid booking is found
                    if (relatedBooking != null) {
                        String bookingID = "B" + (bookings.indexOf(relatedBooking) + 1);  // Generate Booking_ID
                        String customerName = relatedBooking.getUser().getUserid();       // Customer's UserID
                        String roomID = relatedBooking.getHall().getHallName();           // Hall Name (Room_ID)
    
                        // Construct feedback in the required format
                        String feedback = bookingID + "," + customerName + "," + roomID + "," + issueDescription;
    
                        // Write feedback to feedback.txt
                        try (PrintWriter writer = new PrintWriter(new FileOutputStream("feedback.txt", true))) {
                            writer.println(feedback); // Append feedback to the file
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(issueFrame, "Error writing feedback data.");
                        }
    
                        // Show success message
                        JOptionPane.showMessageDialog(issueFrame, "Issue reported successfully!");
    
                        // Close the issue frame
                        issueFrame.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(issueFrame, "No booking found for this hall.");
                    }
                } else {
                    JOptionPane.showMessageDialog(issueFrame, "Please select a hall.");
                }
            }
        });
    
        issueFrame.setVisible(true);
    }
        
    private void showViewBookingsUI() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("E dd/MM/yyyy"); // Format for the date
        StringBuilder bookingDetails = new StringBuilder("Your Bookings:\n");
    
        for (Booking booking : bookings) {
            // Format the date
            String formattedDate = dateFormat.format(booking.getBookingDate());
    
            // Get the start time of the first session and the end time of the last session
            String sessionStart = booking.getSessions().get(0).split(" - ")[0];
            String sessionEnd = booking.getSessions().get(booking.getSessions().size() - 1).split(" - ")[1];
            String bookingTime = sessionStart + " - " + sessionEnd;
    
            // Construct the display string
            bookingDetails.append("Hall: ").append(booking.getHall().getHallName())
                    .append(", Date: ").append(formattedDate)
                    .append(", Time: ").append(bookingTime)
                    .append("\n");
        }
    
        JOptionPane.showMessageDialog(this, bookingDetails.toString());
    }
    



    // Load hall data based on hall type and availability for the selected date
    private void loadHallData(String hallType, Date bookingDate) {
        tableModel.setRowCount(0); // Clear existing data
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String bookingDateString = dateFormat.format(bookingDate);

        for (Hall hall : halls) {
            if (hall.getHallType().equalsIgnoreCase(hallType)) {
                String availableTime = getAvailableTimeFromFile(hall, bookingDateString);  // Get availability from file
                Object[] rowData = {hall.getHallName(), hall.getHallType(), hall.getCapacity(), hall.getRatePerHour(), availableTime.split(",")[0], availableTime.split(",")[1], false};
                tableModel.addRow(rowData);
            }
        }
    }

    // Get availability for the hall on the selected date from availability.txt
    private String getAvailableTimeFromFile(Hall hall, String bookingDateString) {
        String availableFrom = "08:00";
        String availableTill = "18:00";

        try (Scanner scanner = new Scanner(new File("availability.txt"))) {
            while (scanner.hasNextLine()) {
                String[] availabilityData = scanner.nextLine().split(",");
                if (availabilityData[0].equals(hall.getHallName()) && availabilityData[1].equals(bookingDateString)) {
                    availableFrom = availabilityData[2];
                    availableTill = availabilityData[3];
                    break;
                }
            }
        } catch (Exception e) {
            // If no entry in availability.txt, return default time (08:00-18:00)
        }

        return availableFrom + "," + availableTill;
    }

    // Update availability after a booking
    private void updateAvailability(Hall hall, Date bookingDate, String bookingStartTime, String bookingEndTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String bookingDateString = dateFormat.format(bookingDate);
    
        List<String> updatedAvailabilityLines = new ArrayList<>();
    
        try {
            // Read the current availability file
            List<String> availabilityLines = Files.readAllLines(Paths.get("availability.txt"));
    
            boolean updated = false;
    
            for (String line : availabilityLines) {
                String[] parts = line.split(",");
                String hallName = parts[0];
                String date = parts[1];
                String availableFrom = parts[2];
                String availableTill = parts[3];
    
                // If the entry matches the hall and booking date, update availability
                if (hallName.equals(hall.getHallName()) && date.equals(bookingDateString)) {
                    if (bookingStartTime.equals(availableFrom)) {
                        // Booking starts at the same time as availableFrom, so adjust availableFrom
                        availableFrom = bookingEndTime;
                    } else if (bookingEndTime.equals(availableTill)) {
                        // Booking ends at the same time as availableTill, so adjust availableTill
                        availableTill = bookingStartTime;
                    }
    
                    // Only add the line if there's still availability after the booking
                    if (!availableFrom.equals(availableTill)) {
                        updatedAvailabilityLines.add(hall.getHallName() + "," + bookingDateString + "," + availableFrom + "," + availableTill);
                    }
    
                    updated = true;
                } else {
                    // Keep the line unchanged if it doesn't match the booking date or hall
                    updatedAvailabilityLines.add(line);
                }
            }
    
            // If there was no existing entry for this hall on this date, add a new one
            if (!updated) {
                // The hall is booked from bookingStartTime to bookingEndTime, so update availability
                String remainingAvailableFrom = bookingEndTime;
                updatedAvailabilityLines.add(hall.getHallName() + "," + bookingDateString + "," + remainingAvailableFrom + ",18:00");
            }
    
            // Write the updated availability list back to the file
            Files.write(Paths.get("availability.txt"), updatedAvailabilityLines);
    
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error updating availability file: " + e.getMessage());
        }
    }
    

    // Process the selected hall for booking
    private void processSelectedHall(Date bookingDate) {
        int selectedRow = -1;

        // Find selected hall
        for (int i = 0; i < hallTable.getRowCount(); i++) {
            if ((boolean) hallTable.getValueAt(i, 6)) { // Check the checkbox
                selectedRow = i;
                break;
            }
        }

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a hall.");
            return;
        }

        // Get selected hall details
        String selectedHall = (String) hallTable.getValueAt(selectedRow, 0); // Hall name
        showSessionSelectionUI(selectedHall, bookingDate);
    }

    private void showSessionSelectionUI(String selectedHall, Date bookingDate) {
        JFrame sessionFrame = new JFrame("Select Sessions");
        sessionFrame.setSize(400, 300);
        sessionFrame.setLayout(null);
        sessionFrame.setLocation(600, 300);

        JLabel instructionLabel = new JLabel("Select available sessions:");
        instructionLabel.setBounds(30, 10, 300, 30);
        sessionFrame.add(instructionLabel);

        // Define the session times
        JCheckBox session1 = new JCheckBox("Session 1 (08:00 - 10:00)");
        session1.setBounds(30, 50, 300, 30);

        JCheckBox session2 = new JCheckBox("Session 2 (10:00 - 12:00)");
        session2.setBounds(30, 80, 300, 30);

        JCheckBox session3 = new JCheckBox("Session 3 (12:00 - 14:00)");
        session3.setBounds(30, 110, 300, 30);

        JCheckBox session4 = new JCheckBox("Session 4 (14:00 - 16:00)");
        session4.setBounds(30, 140, 300, 30);

        JCheckBox session5 = new JCheckBox("Session 5 (16:00 - 18:00)");
        session5.setBounds(30, 170, 300, 30);

        // Add the checkboxes to the frame
        sessionFrame.add(session1);
        sessionFrame.add(session2);
        sessionFrame.add(session3);
        sessionFrame.add(session4);
        sessionFrame.add(session5);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setBounds(150, 210, 100, 30);
        sessionFrame.add(confirmButton);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> selectedSessions = new ArrayList<>();

                if (session1.isSelected()) selectedSessions.add("08:00 - 10:00");
                if (session2.isSelected()) selectedSessions.add("10:00 - 12:00");
                if (session3.isSelected()) selectedSessions.add("12:00 - 14:00");
                if (session4.isSelected()) selectedSessions.add("14:00 - 16:00");
                if (session5.isSelected()) selectedSessions.add("16:00 - 18:00");

                if (selectedSessions.isEmpty()) {
                    JOptionPane.showMessageDialog(sessionFrame, "Please select at least one session.");
                } else {
                    handleBookingConfirmation(selectedHall, bookingDate, selectedSessions);
                    sessionFrame.setVisible(false);
                }
            }
        });

        sessionFrame.setVisible(true);
    }

    private void handleBookingConfirmation(String selectedHall, Date bookingDate, List<String> selectedSessions) {
        // Find the selected hall
        Hall hall = halls.stream()
                .filter(h -> h.getHallName().equals(selectedHall))
                .findFirst()
                .orElse(null);

        if (hall == null) {
            JOptionPane.showMessageDialog(this, "Hall not found!");
            return;
        }

        // Create a new booking and add it to the system
        String bookingID = "B" + (bookings.size() + 1);  // Generate a simple booking ID
        String sessionStart = selectedSessions.get(0).split(" - ")[0];
        String sessionEnd = selectedSessions.get(selectedSessions.size() - 1).split(" - ")[1];
        String bookingTime = sessionStart + " - " + sessionEnd;

        Booking newBooking = new Booking(currentUser, hall, bookingDate, selectedSessions);
        bookings.add(newBooking);

        // Write the new booking to the file
        try (PrintWriter writer = new PrintWriter(new FileOutputStream("bookings.txt", true))) {
            writer.println(currentUser.getUserid() + "," +
                    hall.getHallName() + "," +
                    bookingID + "," +
                    new SimpleDateFormat("yyyy-MM-dd").format(bookingDate) + "," +
                    bookingTime + ",PAID");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error writing booking data.");
        }

        // Update availability for the hall on the selected date
        updateAvailability(hall, bookingDate, sessionStart, sessionEnd);

        JOptionPane.showMessageDialog(this, "Booking confirmed! Hall: " + selectedHall + " from " + bookingTime);
    }

    // Other methods for viewing, canceling bookings, etc.
    // Additional methods go here...

    private String determineHallType(int pax) {
        if (pax > 0 && pax <= 30) {
            return "Meeting Room";
        } else if (pax > 30 && pax <= 300) {
            return "Banquet Hall";
        } else if (pax > 300 && pax <= 1000) {
            return "Auditorium";
        } else {
            return null; // No suitable hall type
        }
    }
}
