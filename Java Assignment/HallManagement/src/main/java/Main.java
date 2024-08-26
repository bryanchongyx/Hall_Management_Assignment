
public class Main {
    public static PageLoginSignIn a1;
    public static PageAdmin a2;
    public static PageRegistrationForm a3;
    public static PageSuperAdmin a4;
    public static PageSchedulerManagement a5;
    public static PageCustomerManagement a6;
    public static PageManagerManagement a7;


    public static void main(String[] args) {
        DataIO.read();
        a1 = new PageLoginSignIn();
    }
}
