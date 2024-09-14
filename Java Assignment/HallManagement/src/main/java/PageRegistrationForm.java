import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;



public class PageRegistrationForm implements ActionListener {
    JFrame a;
    JTextField fullnameField, emailField, phoneField, useridField, passwordField;
    JButton registerButton, backButton;

    public PageRegistrationForm() {
        a = new JFrame("Registration Form");
        a.setSize(400, 300);
        a.setLocation(600, 300);
        a.setLayout(new GridLayout(7, 2, 10, 10));

        // Initialize form components
        fullnameField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();
        useridField = new JTextField();
        passwordField = new JTextField();

        registerButton = new JButton("Register");
        backButton = new JButton("Back");

        // Add components to the form
        a.add(new JLabel("Full Name:"));
        a.add(fullnameField);
        a.add(new JLabel("Email:"));
        a.add(emailField);
        a.add(new JLabel("Phone Number (starts with 0, 10 digits):"));
        a.add(phoneField);
        a.add(new JLabel("User ID:"));
        a.add(useridField);
        a.add(new JLabel("Password:"));
        a.add(passwordField);
        a.add(registerButton);
        a.add(backButton);

        // Set action listeners
        registerButton.addActionListener(this);
        backButton.addActionListener(this);

        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            try {
                String reg_fullname = fullnameField.getText().trim();
                String reg_userid = useridField.getText().trim();
                String reg_password = passwordField.getText().trim();
                String reg_email = emailField.getText().trim();
                String reg_phone = phoneField.getText().trim();

                // Input validation
                if (reg_fullname.isEmpty() || reg_email.isEmpty() || reg_phone.isEmpty() || reg_userid.isEmpty() || reg_password.isEmpty()) {
                    throw new Exception("All fields are required.");
                }
                if (!reg_email.contains("@") || !reg_email.endsWith(".com")) {
                    throw new Exception("Invalid email format.");
                }
                if (reg_phone.length() != 10 || !reg_phone.matches("\\d+")) {
                    throw new Exception("Phone number must be 10 digits long.");
                }
                if (DataIO.checkUserid(reg_userid) != null) {
                    throw new Exception("User ID is already taken!");
                }
                if (DataIO.checkCustomerEmail(reg_email) != null) {
                    throw new Exception("Email is already registered!");
                }
                if (DataIO.checkCustomerPhone(reg_phone) != null) {
                    throw new Exception("Phone number is already registered!");
                }

                // Register the new user
                DataIO.allUser.add(new User(reg_fullname,reg_userid, reg_password, "customer"));
                DataIO.allCustomer.add(new Customer(reg_fullname, reg_userid, reg_password, reg_email, reg_phone));
                DataIO.write();

                JOptionPane.showMessageDialog(a, "Account successfully registered.");
                clearFields();
                a.setVisible(false);
                Main.a1.a.setVisible(true); // Go back to login page

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(a, ex.getMessage());
            }
        } else if (e.getSource() == backButton) {
            clearFields();
            a.setVisible(false);
            Main.a1.a.setVisible(true); // Go back to login page
        }
    }
        private void clearFields() {
        fullnameField.setText("");
        useridField.setText("");
        passwordField.setText("");
        emailField.setText("");
        phoneField.setText("");

    }

}
