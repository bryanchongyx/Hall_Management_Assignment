import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class DataIO {
    // Data structures for different user roles and halls
    public static ArrayList<User> allUser = new ArrayList<>();
    public static ArrayList<Customer> allCustomer = new ArrayList<>();
    public static ArrayList<Admin> allAdmin = new ArrayList<>();
    public static ArrayList<Scheduler> allScheduler = new ArrayList<>();
    public static ArrayList<Manager> allManager = new ArrayList<>();
    public static ArrayList<Hall> allHall = new ArrayList<>();
    public static ArrayList<Booking> allBooking = new ArrayList<>();

    // File paths
    private static final String USERS_FILE = "user.txt";
    private static final String CUSTOMERS_FILE = "customer.txt";
    private static final String ADMINS_FILE = "admin.txt";
    private static final String SCHEDULERS_FILE = "scheduler.txt";
    private static final String MANAGERS_FILE = "manager.txt";
    private static final String HALLS_FILE = "halls.txt";
    private static final String BOOKINGS_FILE = "bookings.txt";
    //Manager txt file:
    //Notification_Report.txt
    //Sales_Report.txt
    //Hall_Maintenance_Report.txt
    //Scheduler_Manager_Chat.txt
    

    // Method to read all data from text files
    public static void read() {
        readUsers();
        readCustomers();
        readAdmins();
        readSchedulers();
        readManagers();
        readHalls();
        // If booking data needs to be read, add readBookings() here
    }

    // Method to write all data back to text files
    public static void write() {
        writeUsers();
        writeCustomers();
        writeAdmins();
        writeSchedulers();
        writeManagers();
        writeHalls();
        // If booking data needs to be written, add writeBookings() here
    }

    // User-related methods
    private static void readUsers() {
        try (Scanner scanner = new Scanner(new File(USERS_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] userData = line.split(",");
                if (userData.length == 4) {
                    String fullname = userData[0].trim();
                    String userid = userData[1].trim();
                    String password = userData[2].trim();
                    String roles = userData[3].trim();
                    allUser.add(new User(fullname, userid, password, roles));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading user data.");
        }
    }

    private static void writeUsers() {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(USERS_FILE))) {
            for (User u : allUser) {
                writer.println(u.getFullname() + "," + u.getUserid() + "," + u.getPassword() + "," + u.getRoles());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error writing user data.");
        }
    }

    // Customer-related methods
    private static void readCustomers() {
        try (Scanner scanner = new Scanner(new File(CUSTOMERS_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] customerData = line.split(",");
                if (customerData.length == 5) {
                    String fullname = customerData[2].trim();
                    String userid = customerData[0].trim();
                    String password = customerData[1].trim();
                    String email = customerData[3].trim();
                    String ph_number = customerData[4].trim();
                    allCustomer.add(new Customer(fullname, userid, password, email, ph_number));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading customer data.");
        }
    }

    private static void writeCustomers() {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(CUSTOMERS_FILE))) {
            for (Customer c : allCustomer) {
                writer.println(c.getFullname() + "," + c.getUserid() + "," + c.getPassword() + "," + c.getEmail() + "," + c.getPh_number());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error writing customer data.");
        }
    }
    
        public static void addBooking(Booking booking) {
        allBooking.add(booking);
        writeBookings(); // Write booking data to file
    }
    
        public static void writeBookings() {
        try (PrintWriter writer = new PrintWriter("bookings.txt")) {
            for (Booking booking : allBooking) {
                writer.println(booking.getUser().getUserid() + "," +
                               booking.getHall().getHallName() + "," +
                               booking.getBookingDate() + "," +
                               booking.isPaid() + "," +
                               booking.isCancelled());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error writing booking data.");
        }
    }

    // Admin-related methods
    private static void readAdmins() {
        try (Scanner scanner = new Scanner(new File(ADMINS_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] adminData = line.split(",");
                if (adminData.length == 4) {
                    String fullname = adminData[0].trim();
                    String userid = adminData[1].trim();
                    String password = adminData[2].trim();
                    String joinedDate = adminData[3].trim();
                    allAdmin.add(new Admin(fullname, userid, password, joinedDate));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading admin data.");
        }
    }

    private static void writeAdmins() {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(ADMINS_FILE))) {
            for (Admin admin : allAdmin) {
                writer.println(admin.getFullname() + "," + admin.getUserid() + "," + admin.getPassword() + "," + admin.getDate());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error writing admin data.");
        }
    }

    // Scheduler-related methods
    private static void readSchedulers() {
        try (Scanner scanner = new Scanner(new File(SCHEDULERS_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] schedulerData = line.split(",");
                if (schedulerData.length == 4) {
                    String fullname = schedulerData[0].trim();
                    String userid = schedulerData[1].trim();
                    String password = schedulerData[2].trim();
                    String joinedDate = schedulerData[3].trim();
                    allScheduler.add(new Scheduler(fullname, userid, password, joinedDate));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading scheduler data.");
        }
    }

    private static void writeSchedulers() {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(SCHEDULERS_FILE))) {
            for (Scheduler scheduler : allScheduler) {
                writer.println(scheduler.getFullname() + "," + scheduler.getUserid() + "," + scheduler.getPassword() + "," + scheduler.getJoinedDate());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error writing scheduler data.");
        }
    }

    // Manager-related methods
    private static void readManagers() {
        try (Scanner scanner = new Scanner(new File(MANAGERS_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] managerData = line.split(",");
                if (managerData.length == 4) {
                    String fullname = managerData[0].trim();
                    String userid = managerData[1].trim();
                    String password = managerData[2].trim();
                    String joinedDate = managerData[3].trim();
                    allManager.add(new Manager(fullname, userid, password, joinedDate));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading manager data.");
        }
    }

    private static void writeManagers() {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(MANAGERS_FILE))) {
            for (Manager manager : allManager) {
                writer.println(manager.getFullname() + "," + manager.getUserid() + "," + manager.getPassword() + "," + manager.getJoinedDate());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error writing manager data.");
        }
    }

    // Hall-related methods
    private static void readHalls() {
        try (Scanner scanner = new Scanner(new File(HALLS_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] hallData = line.split(",");
                if (hallData.length == 4) {
                    String hallName = hallData[0].trim();
                    String hallType = hallData[1].trim();
                    int capacity = Integer.parseInt(hallData[2].trim());
                    double ratePerHour = Double.parseDouble(hallData[3].trim());
                    Hall hall = new Hall(hallName, hallType, capacity, ratePerHour);
                    allHall.add(hall);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading hall data.");
        }
    }

    private static void writeHalls() {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(HALLS_FILE))) {
            for (Hall hall : allHall) {
                writer.println(hall.getHallName() + "," + hall.getHallType() + "," + hall.getCapacity() + "," + hall.getRatePerHour());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error writing hall data.");
        }
    }

    // User lookup methods
    public static User checkUserid(String s) {
        for (User u : allUser) {
            if (s.equals(u.getUserid())) {
                return u;
            }
        }
        return null;
    }

    // Method to check the role of a user by ID
    public static String checkUserRole(String userid) {
        for (User u : allUser) {
            if (userid.equals(u.getUserid())) {
                return u.getRoles();
            }
        }
        return null;
    }

    // Customer lookup methods
    public static Customer checkCustomerUserid(String s) {
        for (Customer c : allCustomer) {
            if (s.equals(c.getUserid())) {
                return c;
            }
        }
        return null;
    }

    public static Customer checkCustomerEmail(String s) {
        for (Customer c : allCustomer) {
            if (s.equals(c.getEmail())) {
                return c;
            }
        }
        return null;
    }

    public static Customer checkCustomerPhone(String phone) {
        for (Customer c : allCustomer) {
            if (c.getPh_number().equals(phone)) {
                return c;
            }
        }
        return null;
    }

    // Admin lookup methods
    public static Admin checkAdminUserid(String s) {
        for (Admin admin : allAdmin) {
            if (s.equals(admin.getUserid())) {
                return admin;
            }
        }
        return null;
    }

    public static Admin findAdminByUserId(String userid) {
        for (Admin admin : allAdmin) {
            if (admin.getUserid().equals(userid)) {
                return admin;
            }
        }
        return null;
    }

    //Find superadmin by User ID
    public static User findSuperAdminByUserid(String userid) {
        for (User user : allUser) { // Assuming allUser is a List<User> containing all users
            if (user.getUserid().equals(userid) && user.getRoles().equals ("superadmin")) {
                return (User) user; // Cast the User to SuperAdmin
            }
        }
        return null; // Return null if no matching SuperAdmin is found
    }

    // Scheduler lookup methods
    public static Scheduler checkSchedulerUserid(String username) {
        for (Scheduler scheduler : allScheduler) {
            if (username.equals(scheduler.getUserid())) {
                return scheduler;
            }
        }
        return null;
    }

    public static Scheduler findSchedulerByUserid(String userid) {
        for (Scheduler scheduler : allScheduler) {
            if (scheduler.getUserid().equals(userid)) {
                return scheduler;
            }
        }
        return null;
    }

    // Manager lookup methods
    public static Manager findManagerByUserid(String userid) {
        for (Manager manager : allManager) {
            if (manager.getUserid().equals(userid)) {
                return manager;
            }
        }
        return null;
    }

    // Method to remove a user by user ID
    public static void removeUserByUserId(String userid) {
        allUser.removeIf(user -> user.getUserid().equals(userid));
    }

    // Method to remove a scheduler by username
    public static void removeSchedulerByUsername(String userid) {
        Scheduler schedulerToRemove = checkSchedulerUserid(userid);
        if (schedulerToRemove != null) {
            allScheduler.remove(schedulerToRemove);
        }
    }

    // Update User UserID
    public static void updateUserUserid(String oldUserid, String newUserid) {
        for (User user : allUser) {
            if (user.getUserid().equals(oldUserid)) {
                user.setUserid(newUserid);
                break;
            }
        }
    }

    // Update Scheduler UserID
    public static void updateSchedulerUserid(String oldUserid, String newUserid) {
        for (Scheduler scheduler : allScheduler) {
            if (scheduler.getUserid().equals(oldUserid)) {
                scheduler.setUserid(newUserid);
                break;
            }
        }
        updateUserUserid(oldUserid, newUserid); // Update in users list
    }

    // Update Manager UserID
    public static void updateManagerUserid(String oldUserid, String newUserid) {
        for (Manager manager : allManager) {
            if (manager.getUserid().equals(oldUserid)) {
                manager.setUserid(newUserid);
                break;
            }
        }
        updateUserUserid(oldUserid, newUserid); // Update in users list
    }
    
    //Update Admin UserID
    public static void updateAdminUserid(String oldUserid, String newUserid) {
        for (Admin admin : allAdmin) {
            if (admin.getUserid().equals(oldUserid)) {
                admin.setUserid(newUserid);
                break;
            }
        }
        updateUserUserid (oldUserid, newUserid); //Update in users list
    }
    
    // Update User Fullname
    public static void updateUserFullname(String oldUserid, String newFullname) {
        for (User user : allUser) {
            if (user.getUserid().equals(oldUserid)) {
                user.setFullname(newFullname);
                break;
            }
        }
    }

    // Update Scheduler Fullname
    public static void updateSchedulerFullname(String userid, String newFullname) {
        for (Scheduler scheduler : allScheduler) {
            if (scheduler.getUserid().equals(userid)) {
                scheduler.setFullname(newFullname);
                break;
            }
        }
        updateUserFullname(userid, newFullname); // Update in users list
    }

    // Update Manager Fullname
    public static void updateManagerFullname(String userid, String newFullname) {
        for (Manager manager : allManager) {
            if (manager.getUserid().equals(userid)) {
                manager.setFullname(newFullname);
                break;
            }
        }
        updateUserFullname(userid, newFullname); // Update in users list
    }

    // Update Admin Fullname
    public static void updateAdminFullname(String userid, String newFullname) {
        for (Admin admin : allAdmin) {
            if (admin.getUserid().equals(userid)) {
                admin.setFullname(newFullname);
                break;
            }
        }
        updateUserFullname(userid, newFullname); // Update in users list
    }

    // Update User Password
    public static void updateUserPassword(String userid, String newPassword) {
        for (User user : allUser) {
            if (user.getUserid().equals(userid)) {
                user.setPassword(newPassword);
                break;
            }
        }
    }

    // Update Scheduler Password
    public static void updateSchedulerPassword(String userid, String newPassword) {
        for (Scheduler scheduler : allScheduler) {
            if (scheduler.getUserid().equals(userid)) {
                scheduler.setPassword(newPassword);
                break;
            }
        }
        updateUserPassword(userid, newPassword); // Update in users list
    }

    // Update Manager Password
    public static void updateManagerPassword(String userid, String newPassword) {
        for (Manager manager : allManager) {
            if (manager.getUserid().equals(userid)) {
                manager.setPassword(newPassword);
                break;
            }
        }
        updateUserPassword(userid, newPassword); // Update in users list
    }

    // Update Admin Password
    public static void updateAdminPassword(String userid, String newPassword) {
        for (Admin admin : allAdmin) {
            if (admin.getUserid().equals(userid)) {
                admin.setPassword(newPassword);
                break;
            }
        }
        updateUserPassword(userid, newPassword); // Update in users list
    }

    // Method to find a hall by name
    public static Hall findHallByName(String hallName) {
        for (Hall hall : allHall) {
            if (hall.getHallName().equalsIgnoreCase(hallName)) {
                return hall;
            }
        }
        return null;
    }
}
