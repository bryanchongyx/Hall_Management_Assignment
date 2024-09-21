import java.util.Date;
import java.util.List;

public class Booking {
    private User user;
    private Hall hall;
    private Date bookingDate;
    private String bookingTime; // Assuming this is a String; adjust as needed.
    private boolean isPaid;
    private boolean isCancelled;
    private List<String> sessions; // New field for storing sessions


    // Constructor
    public Booking(User user, Hall hall, Date bookingDate, List<String> sessions) {
        this.user = user;
        this.hall = hall;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.sessions = sessions;
        this.isPaid = false;
        this.isCancelled = false;
    }

    // Method to make a payment
    public void makePayment() {
        this.isPaid = true;
    }

    // Method to check if booking can be cancelled
    public boolean canBeCancelled() {
        Date today = new Date();
        long diff = bookingDate.getTime() - today.getTime();
        return diff >= 3 * 24 * 60 * 60 * 1000L; // 3 days in milliseconds
    }

    // Method to cancel a booking
    public void cancelBooking() throws Exception {
        if (canBeCancelled()) {
            this.isCancelled = true;
        } else {
            throw new Exception("Booking cannot be cancelled within 3 days of the booking date.");
        }
    }

    // Getters and setters

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    // Getter and Setter for sessions
    public List<String> getSessions() {
        return sessions;
    }

    public void setSessions(List<String> sessions) {
        this.sessions = sessions;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }
}
