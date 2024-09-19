public class Main {
    public static PageLoginSignIn a1;
    public static PageAdmin a2;
    public static PageRegistrationForm a3;
    public static PageSuperAdmin a4;
    public static PageSchedulerManagement a5;
    public static PageCustomerManagement a6;
    public static PageManagerManagement a7;
    public static PageBookingSystem a8;
    public static PageScheduler a9;
    public static PageEditAdminProfile a10;
    public static PageAdminManagement a11;

    public static User defaultUser; // Keep track of the logged-in user

    public static void main(String[] args) {
        DataIO.read(); // Read all the data
        a1 = new PageLoginSignIn();
        // Assuming defaultUser gets set after successful login
        defaultUser = DataIO.checkUserid("someUserId"); // Example of getting the user

    }
}
