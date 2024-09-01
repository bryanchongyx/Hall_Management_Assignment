package redo.java.assign;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataIO {
    public static List<User> allUsers = new ArrayList<>();
    public static List<Customer> allCustomers = new ArrayList<>();
    public static List<Admin> allAdmins = new ArrayList<>();
    public static List<Scheduler> allSchedulers = new ArrayList<>();
    public static List<Manager> allManagers = new ArrayList<>();
    public static List<Hall> allHalls = new ArrayList<>();
    public static List<Booking> allBookings = new ArrayList<>();

    // File paths
    private static final String USERS_FILE = "users.dat";
    private static final String CUSTOMERS_FILE = "customers.dat";
    private static final String ADMINS_FILE = "admins.dat";
    private static final String SCHEDULERS_FILE = "schedulers.dat";
    private static final String MANAGERS_FILE = "managers.dat";
    private static final String HALLS_FILE = "halls.dat";
    private static final String BOOKINGS_FILE = "bookings.dat";

    // Method to read all data
    public static void readAllData() {
        allUsers = readData(USERS_FILE);
        allCustomers = readData(CUSTOMERS_FILE);
        allAdmins = readData(ADMINS_FILE);
        allSchedulers = readData(SCHEDULERS_FILE);
        allManagers = readData(MANAGERS_FILE);
        allHalls = readData(HALLS_FILE);
        allBookings = readData(BOOKINGS_FILE);
    }

    // Method to write all data
    public static void writeAllData() {
        writeData(allUsers, USERS_FILE);
        writeData(allCustomers, CUSTOMERS_FILE);
        writeData(allAdmins, ADMINS_FILE);
        writeData(allSchedulers, SCHEDULERS_FILE);
        writeData(allManagers, MANAGERS_FILE);
        writeData(allHalls, HALLS_FILE);
        writeData(allBookings, BOOKINGS_FILE);
    }

    // Generic method to read data
    @SuppressWarnings("unchecked")
    private static <T> List<T> readData(String fileName) {
        List<T> dataList = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            dataList = (List<T>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println(fileName + " not found. Creating new file.");
            writeData(dataList, fileName); // Create empty file if not found
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error reading " + fileName);
        }
        return dataList;
    }

    // Generic method to write data
    private static <T> void writeData(List<T> dataList, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(dataList);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error writing to " + fileName);
        }
    }

    // User-related methods
    public static User checkUserId(String userId) {
        for (User user : allUsers) {
            if (user.getUserId().equalsIgnoreCase(userId)) {
                return user;
            }
        }
        return null;
    }

    public static void addUser(User user) {
        allUsers.add(user);
        writeData(allUsers, USERS_FILE);
    }

    // Booking-related methods
    public static List<Booking> getBookingsByUser(User user) {
        List<Booking> userBookings = new ArrayList<>();
        for (Booking booking : allBookings) {
            if (booking.getUser().equals(user)) {
                userBookings.add(booking);
            }
        }
        return userBookings;
    }

    public static void addBooking(Booking booking) {
        allBookings.add(booking);
        writeData(allBookings, BOOKINGS_FILE);
    }

    public static void removeBooking(Booking booking) {
        allBookings.remove(booking);
        writeData(allBookings, BOOKINGS_FILE);
    }

    // Hall-related methods
    public static Hall findHallByName(String hallName) {
        for (Hall hall : allHalls) {
            if (hall.getHallName().equalsIgnoreCase(hallName)) {
                return hall;
            }
        }
        return null;
    }

    public static void addHall(Hall hall) {
        allHalls.add(hall);
        writeData(allHalls, HALLS_FILE);
    }
}
