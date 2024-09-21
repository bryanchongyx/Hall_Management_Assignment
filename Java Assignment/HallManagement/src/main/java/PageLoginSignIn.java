import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PageLoginSignIn implements ActionListener {
    JFrame a;
    Button login, register;

    public PageLoginSignIn() {
        a = new JFrame();
        a.setTitle("Login & Register");
        a.setSize(300, 150);
        a.setLocation(625, 400);
        a.setLayout(new FlowLayout());

        login = new Button("Login");
        register = new Button("Register");

        login.addActionListener(this);
        register.addActionListener(this);

        a.add(login);
        a.add(register);
        a.setVisible(true);
        
        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == login) {
                String userid_input = JOptionPane.showInputDialog("Enter userid: ");
                User foundUser = DataIO.checkUserid(userid_input);

                if (foundUser == null) {
                    throw new Exception("User not found");
                }

                String password_input = JOptionPane.showInputDialog("Enter password: ").trim();
                if (!password_input.equals(foundUser.getPassword())) {
                    throw new Exception("Password is incorrect");
                }

                // Hide the login page
                a.setVisible(false);
                // When login is successful
                Main.setLoggedInUser(foundUser);  // Set the logged-in user in the Main class

                // Check user role
                String role = foundUser.getRoles(); // Updated to get role from foundUser

                // Show the appropriate page based on the role
                if ("superadmin".equals(role)) {
                    User user = DataIO.findSuperAdminByUserid(userid_input);
                    if (Main.a4 == null) {
                        Main.a4 = new PageSuperAdmin(user); // Instantiate if not already created
                    }
                    Main.a4.a.setVisible(true);

                } else if ("administrator".equals(role)) {
                    Admin loggedAdmin = DataIO.findAdminByUserId(userid_input);
                    if (loggedAdmin == null){
                        throw new Exception ("Admin details not found");
                    }
                    if (Main.a2 == null){
                        Main.a2 = new PageAdmin (loggedAdmin);
                    }
                    Main.a2.a.setVisible(true);
                } else if ("scheduler".equals(role)) {
                    if(Main.a9 == null){
                        Main.a9 = new PageScheduler();
                    }
                } else if ("customer".equals(role)) {
                    // Only instantiate and show BookingSystem if the user is a customer
                    if (Main.a8 == null) {
                        Main.a8 = new PageBookingSystem(foundUser); // Instantiate BookingSystem with the logged-in user
                    }
                    Main.a8.setVisible(true);
                } else if ("manager".equals(role)) {
                    JOptionPane.showMessageDialog(a, "Not ready yet");
                } else {
                    throw new Exception("Unrecognized role");
                }

            } else if (e.getSource() == register) {
                // Hide login page and show registration page
                a.setVisible(false);
                if (Main.a3 == null) {
                    Main.a3 = new PageRegistrationForm(); // Instantiate if not already created
                }
                Main.a3.a.setVisible(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(a, ex.getMessage());
        }
    }

    public void setVisible(boolean b) {
        a.setVisible(b);
    }
}