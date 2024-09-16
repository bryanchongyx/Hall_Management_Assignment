import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PageBookingSystem extends JFrame {
    private List<Hall> halls;
    private List<Booking> bookings;
    private User currentUser;

    public PageBookingSystem(User user) {
        this.currentUser = user;
        this.halls = DataIO.allHall; // Assuming you load halls from DataIO
        this.bookings = DataIO.allBooking; // Assuming you load bookings from DataIO
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Hall Booking System");
        setSize(600, 400);
        setLocation (525, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

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

        JButton logoutButton = new JButton ("Logout");
        logoutButton.setBounds (50, 250, 150, 30);
        add (logoutButton);

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
        JFrame bookingFrame = new JFrame("Book a Hall");
        bookingFrame.setSize(400, 400);
        bookingFrame.setLayout(null);
        bookingFrame.setLocation (600,300);

        JButton auditoriumButton = new JButton("Auditorium");
        auditoriumButton.setBounds(50, 50, 150, 30);
        bookingFrame.add(auditoriumButton);

        JButton meetingRoomButton = new JButton("Meeting Room");
        meetingRoomButton.setBounds(50, 100, 150, 30);
        bookingFrame.add(meetingRoomButton);

        JButton banquetHallButton = new JButton("Banquet Hall");
        banquetHallButton.setBounds(50, 150, 150, 30);
        bookingFrame.add(banquetHallButton);

        JButton backButton = new JButton ("Back");
        backButton.setBounds (50, 200, 150, 30);
        bookingFrame.add (backButton);

        auditoriumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBooking("Auditorium");
            }
        });

        meetingRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBooking("Meeting Room");
            }
        });

        banquetHallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBooking("Banquet Hall");
            }
        });

        backButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed (ActionEvent e){
                bookingFrame.dispose();
                setVisible (true);
                
            }
        });

        bookingFrame.setVisible(true);
    }

    private void handleBooking(String hallType) {
    // Validate user input for hall type
    if (hallType == null || hallType.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please select a valid hall type.");
        return;
    }

    // Find the selected hall
    Hall selectedHall = halls.stream()
                             .filter(hall -> hall.getHallType().equalsIgnoreCase(hallType))
                             .findFirst()
                             .orElse(null);

    if (selectedHall == null) {
        JOptionPane.showMessageDialog(this, "Hall type not found!");
        return;
    }

    // Prompt user for booking date and validate input
    String dateStr = JOptionPane.showInputDialog("Enter Date (YYYY-MM-DD):");
    if (dateStr == null || dateStr.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Date cannot be empty.");
        return;
    }

    // Parse and validate booking date
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date bookingDate;
    try {
        bookingDate = dateFormat.parse(dateStr);
    } catch (ParseException ex) {
        JOptionPane.showMessageDialog(this, "Invalid date format.");
        return;
    }

    // Prompt user for booking time and validate input
    String time = JOptionPane.showInputDialog("Enter Time (HH:MM):");
    if (time == null || time.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Time cannot be empty.");
        return;
    }

    // Create a new booking and add it to the system
    Booking newBooking = new Booking(currentUser, selectedHall, bookingDate, time);
    bookings.add(newBooking);
    DataIO.addBooking(newBooking);  // Add booking to the data storage

    // Confirm booking and proceed to payment
    JOptionPane.showMessageDialog(this, "Booking confirmed for " + hallType + " on " + dateStr + " at " + time + ". Payment: $" + calculateAmount(hallType));
    showPaymentUI();
}


    private void showPaymentUI() {
        JOptionPane.showMessageDialog(this, "Payment UI Placeholder");
        // Implement actual payment logic here
    }

    private void showViewBookingsUI() {
        StringBuilder bookingDetails = new StringBuilder("Your Bookings:\n");
        for (Booking booking : bookings) {
            bookingDetails.append("Hall: ").append(booking.getHall().getHallName())
                    .append(", Date: ").append(booking.getBookingDate())
                    .append(", Time: ").append(booking.getBookingTime())
                    .append("\n");
        }
        JOptionPane.showMessageDialog(this, bookingDetails.toString());
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
                bookings.remove(i); // Remove from the list
                bookingFound = true;
                JOptionPane.showMessageDialog(this, "Booking for " + bookingToCancel + " has been canceled.");
                
                // Update the data in DataIO
                DataIO.writeBookings(); 
                
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
        issueFrame.setLocation (575,275);

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
        String venue = JOptionPane.showInputDialog("Enter the venue where the issue is:");
        JOptionPane.showMessageDialog(this, "Issue reported: " + issueDescription + " at " + venue);
        // Implement issue handling logic here
    }

    private double calculateAmount(String hallType) {
        // Example pricing logic
        switch (hallType) {
            case "Auditorium":
                return 100.0;
            case "Meeting Room":
                return 50.0;
            case "Banquet Hall":
                return 75.0;
            default:
                return 0.0;
        }
    }
}
