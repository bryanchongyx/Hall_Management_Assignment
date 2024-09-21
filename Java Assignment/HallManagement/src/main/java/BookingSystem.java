import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BookingSystem extends JFrame {
    private List<Hall> halls;
    private List<Booking> bookings;
    private User currentUser;

    public BookingSystem(User user) {
        this.currentUser = user;
        this.halls = DataIO.allHall; // Assuming you load halls from DataIO
        this.bookings = DataIO.allBooking; // Assuming you load bookings from DataIO
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Hall Booking System");
        setSize(600, 400);
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

        setVisible(true);
    }

    private void showBookingUI() {
        JFrame bookingFrame = new JFrame("Book a Hall");
        bookingFrame.setSize(400, 300);
        bookingFrame.setLayout(null);
        bookingFrame.setLocation (400,400);

        JButton auditoriumButton = new JButton("Auditorium");
        auditoriumButton.setBounds(50, 50, 150, 30);
        bookingFrame.add(auditoriumButton);

        JButton meetingRoomButton = new JButton("Meeting Room");
        meetingRoomButton.setBounds(50, 100, 150, 30);
        bookingFrame.add(meetingRoomButton);

        JButton banquetHallButton = new JButton("Banquet Hall");
        banquetHallButton.setBounds(50, 150, 150, 30);
        bookingFrame.add(banquetHallButton);

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

        bookingFrame.setVisible(true);
    }

private void handleBooking(String initialHallType) {
    // Initialize hallType to the passed parameter value
    String hallType = initialHallType;

    // Validate user input for hall type
    if (hallType == null || hallType.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please select a valid hall type.");
        return;
    }

    // Prompt user for number of people
    String paxStr = JOptionPane.showInputDialog("Enter the number of people (pax):");
    int pax;
    try {
        pax = Integer.parseInt(paxStr);
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid number format.");
        return;
    }

    // Automatically assign hall type based on number of pax
    if (pax <= 0) {
        JOptionPane.showMessageDialog(this, "Number of people must be greater than zero.");
        return;
    } else if (pax <= 30) {
        hallType = "Meeting Room";
    } else if (pax <= 300) {
        hallType = "Banquet Hall";
    } else if (pax <= 1000) {
        hallType = "Auditorium";
    } else {
        JOptionPane.showMessageDialog(this, "Sorry, we do not have a hall that will meet your required capacity.");
        return;
    }

    // Capture the final value of hallType for use in the lambda expression
    String finalHallType = hallType;

    // Filter the available halls based on assigned hall type
    List<Hall> availableHalls = halls.stream()
                                      .filter(hall -> hall.getHallType().equalsIgnoreCase(finalHallType))
                                      .collect(Collectors.toList());

    if (availableHalls.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No available halls found for the selected type.");
        return;
    }

    // Display available halls and let the customer select one
    String[] hallOptions = availableHalls.stream()
                                         .map(Hall::getHallName)
                                         .toArray(String[]::new);

    String selectedHallName = (String) JOptionPane.showInputDialog(this, "Select a hall:", 
        "Available Halls", JOptionPane.QUESTION_MESSAGE, null, hallOptions, hallOptions[0]);

    if (selectedHallName == null) {
        JOptionPane.showMessageDialog(this, "No hall selected.");
        return;
    }

    Hall selectedHall = availableHalls.stream()
                                      .filter(hall -> hall.getHallName().equalsIgnoreCase(selectedHallName))
                                      .findFirst()
                                      .orElse(null);

    if (selectedHall == null) {
        JOptionPane.showMessageDialog(this, "Selected hall not found.");
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

    // Prompt user to select sessions
    String[] sessionOptions = {"Session 1 (08:00 - 10:00)", "Session 2 (10:00 - 12:00)",
                               "Session 3 (12:00 - 14:00)", "Session 4 (14:00 - 16:00)",
                               "Session 5 (16:00 - 18:00)"};
    List<String> selectedSessions = new ArrayList<>();

    for (String session : sessionOptions) {
        int response = JOptionPane.showConfirmDialog(this, "Would you like to book " + session + "?", "Select Sessions",
            JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            selectedSessions.add(session);
        }
    }

    if (selectedSessions.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No sessions selected.");
        return;
    }

    // Create a new booking and add it to the system
    Booking newBooking = new Booking(currentUser, selectedHall, bookingDate, selectedSessions);
    bookings.add(newBooking);
    //DataIO.addBooking(newBooking);  // Add booking to the data storage

    // Confirm booking and proceed to payment
    JOptionPane.showMessageDialog(this, "Booking confirmed for " + hallType + " on " + dateStr + 
        ". Sessions: " + selectedSessions + ". Payment: $" + calculateAmount(hallType));
    showPaymentUI();
}

    
    private String determineHallType(int pax) {
        if (pax <= 30) {
            return "Meeting Room";
        } else if (pax <= 300) {
            return "Banquet Hall";
        } else if (pax <= 1000) {
            return "Auditorium";
        } else {
            return null; // No suitable hall type
        }
    }
    
    private String[] getAvailableHallsByType(String hallType) {
        // Logic to filter halls by type and availability
        List<String> availableHalls = new ArrayList<>();
        for (Hall hall : halls) {
            if (hall.getHallType().equalsIgnoreCase(hallType) && isHallAvailable(hall)) {
                availableHalls.add(hall.getHallName());
            }
        }
        return availableHalls.toArray(new String[0]);
    }
    
    private boolean isHallAvailable(Hall hall) {
        // Logic to check against bookings and maintenance schedules
        // This is a placeholder, you should implement the logic to check availability
        return true; // Assume available for now
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
                //DataIO.writeBookings(); 
                
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
