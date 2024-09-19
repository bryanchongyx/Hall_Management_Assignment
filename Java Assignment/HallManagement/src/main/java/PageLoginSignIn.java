import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
public class PageLoginSignIn implements ActionListener {
    JFrame a;
    JButton login, register;
    JTextField userIdField;
    JPasswordField passwordField;
    JLabel messageLabel;
    JCheckBox showPassword;

    public PageLoginSignIn() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        a = new JFrame();
        a.setTitle("Login & Register");
        a.setSize(650, 650);
        a.setLocationRelativeTo(null); // Center the frame
        a.setLayout(new GridBagLayout());
        a.getContentPane().setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Welcome! Please Login:");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        a.add(title, gbc);

        // User ID Label and Field
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        a.add(new JLabel("User ID: "), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        userIdField = new JTextField(15);
        a.add(userIdField, gbc);

        // Password Label and Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        a.add(new JLabel("Password: "), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        passwordField = new JPasswordField(15);
        a.add(passwordField, gbc);

        // Show/Hide Password Checkbox
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        showPassword = new JCheckBox("Show Password");
        showPassword.setBackground(Color.WHITE);
        showPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPassword.isSelected()) {
                    passwordField.setEchoChar((char) 0); // Show password
                } else {
                    passwordField.setEchoChar('•'); // Hide password
                }
            }
        });
        a.add(showPassword, gbc);

        // Login Button
        login = new JButton("Login");
        login.setFont(new Font("Arial", Font.BOLD, 14));
        login.setBackground(Color.LIGHT_GRAY);
        login.setForeground(Color.DARK_GRAY);
        login.setPreferredSize(new Dimension(100, 30));
        login.setToolTipText("Click here to login to your account");
        login.addActionListener(this);

        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        a.add(login, gbc);

        // Message Label for errors
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        a.add(messageLabel, gbc);

        // Register Label and Button
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JLabel registerLabel = new JLabel("Don't have an account?");
        registerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        a.add(registerLabel, gbc);

        gbc.gridy = 7;
        register = new JButton("Register Now");
        register.setFont(new Font("Arial", Font.BOLD, 14));
        register.setBackground(Color.LIGHT_GRAY);
        register.setForeground(Color.DARK_GRAY);
        register.setPreferredSize(new Dimension(150, 30));
        register.setToolTipText("Click here to create a new account");
        register.addActionListener(this);
        a.add(register, gbc);

        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.setVisible(true);
    }
    
    @Override
public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == login) {
                String userid_input = userIdField.getText().trim();
                User foundUser = DataIO.checkUserid(userid_input);

                if (foundUser == null) {
                    throw new Exception("User not found");
                }

                String password_input = new String (passwordField.getPassword()).trim();
                if (!password_input.equals(foundUser.getPassword())) {
                    throw new Exception("Password is incorrect");
                }

                // Hide the login page
                a.setVisible(false);

                // Check user role
                String role = foundUser.getRoles(); // Updated to get role from foundUser
                clearFields();

                // Show the appropriate page based on the role
                if ("superadmin".equals(role)) {
                    User user = DataIO.findSuperAdminByUserid(userid_input);
                    if (Main.a4 == null) {
                        Main.a4 = new PageSuperAdmin(user); // Instantiate if not already created
                    }
                    a.setVisible(false);
                    Main.a4.a.setVisible(true);

                } else if ("administrator".equals(role)) {
                    Admin admin = DataIO.findAdminByUserId(userid_input);
                    if (Main.a2 == null){
                        Main.a2 = new PageAdmin (admin);
                    }
                    a.setVisible (false);
                    Main.a2.a.setVisible (true);

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
        a.setVisible(true);
    }
    public void clearFields(){
        userIdField.setText ("");
        passwordField.setText("");
    }


}
