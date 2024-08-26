import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class DataIO {
    public static ArrayList<User> allUser = new ArrayList<>();
    public static ArrayList<Customer> allCustomer = new ArrayList<>();
    public static ArrayList<Admin> allAdmin = new ArrayList<>();
    public static ArrayList<Scheduler> allScheduler = new ArrayList<>();
    public static ArrayList<Manager> allManager = new ArrayList<>();

    public static void read() {
        try {
            // Reading from user.txt
            Scanner a = new Scanner(new File("user.txt"));
            while (a.hasNextLine()) {
                String line = a.nextLine();
                String[] userData = line.split(",");
                if (userData.length == 4) { // Ensure there are 4 fields
                    String fullname = userData[0].trim();
                    String userid = userData[1].trim();
                    String password = userData[2].trim();
                    String roles = userData[3].trim();
                    allUser.add(new User(fullname, userid, password, roles));
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }

            // Reading from customer.txt
            a = new Scanner(new File("customer.txt"));
            while (a.hasNextLine()) {
                String line = a.nextLine();
                String[] customerData = line.split(",");
                if (customerData.length == 5) {
                    String userid = customerData[0].trim();
                    String password = customerData[1].trim();
                    String fullname = customerData[2].trim();
                    String email = customerData[3].trim();
                    String ph_number = customerData[4].trim();
                    allCustomer.add(new Customer(fullname, userid, password, email, ph_number));
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }

            // Reading from admin.txt
            a = new Scanner(new File("admin.txt"));
            while (a.hasNextLine()) {
                String line = a.nextLine();
                String[] adminData = line.split(",");
                if (adminData.length == 4) {
                    String fullname = adminData[0].trim();
                    String userid = adminData[1].trim();
                    String password = adminData[2].trim();
                    String joinedDate = adminData[3].trim();
                    allAdmin.add(new Admin(fullname, userid, password, joinedDate));
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }

            // Reading from scheduler.txt
            a = new Scanner(new File("scheduler.txt"));
            while (a.hasNextLine()) {
                String line = a.nextLine();
                String[] schedulerData = line.split(",");
                if (schedulerData.length == 4) {
                    String fullname = schedulerData[0].trim();
                    String userid = schedulerData[1].trim();
                    String password = schedulerData[2].trim();
                    String joinedDate = schedulerData[3].trim();
                    allScheduler.add(new Scheduler(fullname, userid, password, joinedDate));
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }

            // Reading from manager.txt
            a = new Scanner(new File("manager.txt"));
            while (a.hasNextLine()) {
                String line = a.nextLine();
                String[] managerData = line.split(",");
                if (managerData.length == 4) {
                    String fullname = managerData[0].trim();
                    String userid = managerData[1].trim();
                    String password = managerData[2].trim();
                    String joinedDate = managerData[3].trim();
                    allManager.add(new Manager(fullname, userid, password, joinedDate));
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }

            a.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading files.");
        }
    }

    // Check if a username already exists in allUser
    public static User checkUserid(String s) {
        for (User u : allUser) {
            if (s.equals(u.getUserid())) {
                return u;
            }
        }
        return null;
    }

    // Check the role of a user by username
    public static String checkUserRole(String userid) {
        for (User u : allUser) {
            if (userid.equals(u.getUserid())) {
                return u.getRoles();
            }
        }
        return null;
    }

    // Check if a customer username already exists
    public static Customer checkCustomerUserid(String s) {
        for (Customer c : allCustomer) {
            if (s.equals(c.getUserid())) {
                return c;
            }
        }
        return null;
    }

    // Check if a customer email is already registered
    public static Customer checkCustomerEmail(String s) {
        for (Customer c : allCustomer) {
            if (s.equals(c.getEmail())) {
                return c;
            }
        }
        return null;
    }

    // Check if a customer phone number is already registered
    public static Customer checkCustomerPhone(String phone) {
        for (Customer c : allCustomer) {
            if (c.getPh_number().equals(phone)) {
                return c;
            }
        }
        return null;
    }

    // Check if an admin username already exists
    public static Admin checkAdminUserid(String s) {
        for (Admin admin : allAdmin) {
            if (s.equals(admin.getUserid())) {
                return admin;
            }
        }
        return null;
    }

    // Check if a scheduler username already exists
    public static Scheduler checkSchedulerUserid(String username) {
        for (Scheduler scheduler : allScheduler) {
            if (username.equals(scheduler.getUserid())) {
                return scheduler;
            }
        }
        return null;
    }

    // Find an admin by user ID
    public static Admin findAdminByUserId(String userid) {
        for (Admin admin : allAdmin) {
            if (admin.getUserid().equals(userid)) {
                return admin;
            }
        }
        return null;
    }

    // Find a scheduler by username
    public static Scheduler findSchedulerByUserid(String userid) {
        for (Scheduler scheduler : allScheduler) {
            if (scheduler.getUserid().equals(userid)) {
                return scheduler;
            }
        }
        return null;
    }

    // Find a manager by username
    public static Manager findManagerByUserid(String userid) {
        for (Manager manager : allManager) {
            if (manager.getUserid().equals(userid)) {
                return manager;
            }
        }
        return null;
    }

    // Remove a user by user ID
    public static void removeUserByUserId(String userid) {
        allUser.removeIf(user -> user.getUserid().equals(userid));
    }

    // Remove a scheduler by username
    public static void removeSchedulerByUsername(String userid) {
        Scheduler schedulerToRemove = checkSchedulerUserid(userid);
        if (schedulerToRemove != null) {
            allScheduler.remove(schedulerToRemove);
        }
    }

    // Update User UserID
    public static void updateUserUserid(String oldUsername, String newUsername) {
        for (User user : allUser) {
            if (user.getUserid().equals(oldUsername)) {
                user.setUserid(newUsername);
                break;
            }
        }
    }

    // Update Scheduler UserID
    public static void updateSchedulerUserid(String oldUsername, String newUsername) {
        for (Scheduler scheduler : allScheduler) {
            if (scheduler.getUserid().equals(oldUsername)) {
                scheduler.setUserid(newUsername);
                break;
            }
        }
        updateUserUserid(oldUsername, newUsername); // Update in users list
    }

    // Update Manager UserID
    public static void updateManagerUserid(String oldUsername, String newUsername) {
        for (Manager manager : allManager) {
            if (manager.getUserid().equals(oldUsername)) {
                manager.setUserid(newUsername);
                break;
            }
        }
        updateUserUserid(oldUsername, newUsername); // Update in users list
    }

    // Writing data back to files
    public static void write() {
        try {
            // Writing to user.txt
            PrintWriter a = new PrintWriter("user.txt");
            for (User u : allUser) {
                // Correct order: fullname, userid, password, roles
                a.println(u.getFullname() + "," + u.getUserid() + "," + u.getPassword() + "," + u.getRoles());
            }
            a.close();

            // Writing to customer.txt
            a = new PrintWriter("customer.txt");
            for (Customer c : allCustomer) {
                // Correct order: fullname, userid, password, email, ph_number
                a.println(c.getFullname() + "," + c.getUserid() + "," + c.getPassword() + "," + c.getEmail() + "," + c.getPh_number());
            }
            a.close();

            // Writing to admin.txt
            a = new PrintWriter("admin.txt");
            for (Admin admin : allAdmin) {
                // Correct order: fullname, userid, password, joinedDate
                a.println(admin.getFullname() + "," + admin.getUserid() + "," + admin.getPassword() + "," + admin.getDate());
            }
            a.close();

            // Writing to scheduler.txt
            a = new PrintWriter("scheduler.txt");
            for (Scheduler scheduler : allScheduler) {
                // Correct order: fullname, userid, password, joinedDate
                a.println(scheduler.getFullname() + "," + scheduler.getUserid() + "," + scheduler.getPassword() + "," + scheduler.getJoinedDate());
            }
            a.close();

            // Writing to manager.txt
            a = new PrintWriter("manager.txt");
            for (Manager manager : allManager) {
                // Correct order: fullname, userid, password, joinedDate
                a.println(manager.getFullname() + "," + manager.getUserid() + "," + manager.getPassword() + "," + manager.getJoinedDate());
            }
            a.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error writing to files.");
        }
    }
}
