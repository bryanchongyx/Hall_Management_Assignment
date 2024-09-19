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

public class PageRegistrationForm implements ActionListener {
    JFrame a;
    JTextField fullnameField, emailField, phoneField, useridField, showPasswordField;
    JPasswordField passwordField; // For hidden password input
    JCheckBox showPasswordCheckbox; // For toggling password visibility
    JButton registerButton, backButton;
    JLabel messageLabel;

    public PageRegistrationForm() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        a = new JFrame("Registration Form");
        a.setSize(450, 400);
        a.setLocationRelativeTo(null); // Center the frame
        a.setLayout(new GridBagLayout());
        a.getContentPane().setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Full Name Label and Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        a.add(new JLabel("Full Name:"), gbc);

        gbc.gridx = 1;
        fullnameField = new JTextField(15);
        a.add(fullnameField, gbc);

        // Email Label and Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        a.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(15);
        a.add(emailField, gbc);

        // Phone Label and Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        a.add(new JLabel("Phone Number:"), gbc);

        gbc.gridx = 1;
        phoneField = new JTextField(15);
        a.add(phoneField, gbc);

        // User ID Label and Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        a.add(new JLabel("User ID:"), gbc);

        gbc.gridx = 1;
        useridField = new JTextField(15);
        a.add(useridField, gbc);

        // Password Label and Field
        gbc.gridx = 0;
        gbc.gridy = 4;
        a.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        a.add(passwordField, gbc);

        // Show Password Checkbox
        gbc.gridy = 5;
        showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.setBackground(Color.WHITE);
        showPasswordCheckbox.addActionListener(this);
        a.add(showPasswordCheckbox, gbc);

        // Register Button
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.LINE_START;
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(Color.LIGHT_GRAY);
        registerButton.setForeground(Color.DARK_GRAY);
        registerButton.setPreferredSize(new Dimension(100, 30));
        registerButton.setToolTipText("Click here to create a new account");
        registerButton.addActionListener(this);
        a.add(registerButton, gbc);

        // Back Button
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setForeground(Color.DARK_GRAY);
        backButton.setPreferredSize(new Dimension(100, 30));
        backButton.setToolTipText("Go back to login page");
        backButton.addActionListener(this);
        a.add(backButton, gbc);

        // Message Label for validation and error messages
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        a.add(messageLabel, gbc);

        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            try {
                String reg_fullname = fullnameField.getText().trim();
                String reg_userid = useridField.getText().trim();
                String reg_password = new String(passwordField.getPassword()).trim(); // Get password
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
                DataIO.allUser.add(new User(reg_fullname, reg_userid, reg_password, "customer"));
                DataIO.allCustomer.add(new Customer(reg_fullname, reg_userid, reg_password, reg_email, reg_phone));
                DataIO.write();

                JOptionPane.showMessageDialog(a, "Account successfully registered.");
                clearFields();
                a.setVisible(false);
                Main.a1.a.setVisible(true); // Go back to login page

            } catch (Exception ex) {
                messageLabel.setText(ex.getMessage());
            }
        } else if (e.getSource() == backButton) {
            clearFields();
            a.setVisible(false);
            Main.a1.a.setVisible(true); // Go back to login page
        } else if (e.getSource() == showPasswordCheckbox) {
            if (showPasswordCheckbox.isSelected()) {
                showPassword();
            } else {
                hidePassword();
            }
        }
    }

    // Method to show password
    private void showPassword() {
        passwordField.setEchoChar((char) 0); // Remove masking
    }

    // Method to hide password
    private void hidePassword() {
        passwordField.setEchoChar('*'); // Set masking character
    }

    private void clearFields() {
        fullnameField.setText("");
        useridField.setText("");
        passwordField.setText("");
        emailField.setText("");
        phoneField.setText("");
        showPasswordCheckbox.setSelected(false); // Reset checkbox
        hidePassword(); // Hide password when clearing fields
    }

}
