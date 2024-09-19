import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class PageEditAdminProfile implements ActionListener {
    JFrame a;
    JTextField fullnameField, useridField, passwordField;
    JButton updateButton, backButton;
    Admin admin;
    PageAdmin adminPage;

    public PageEditAdminProfile(PageAdmin adminPage, Admin admin) {
        this.admin = admin;
        this.adminPage = adminPage;

        a = new JFrame();
        a.setTitle("Edit Admin Profile");
        a.setSize(400, 250);
        a.setLocationRelativeTo(null); // Center the frame
        a.setLayout(new GridBagLayout());
        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.getContentPane().setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create and style components
        JLabel fullnameLabel = new JLabel("Full Name:");
        fullnameField = new JTextField(admin.getFullname(), 20);
        JLabel useridLabel = new JLabel("User ID:");
        useridField = new JTextField(admin.getUserid(), 20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(admin.getPassword(), 20); // Use JPasswordField for password

        updateButton = new JButton("Update");
        backButton = new JButton("Back");

        // Add components to the frame
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        a.add(fullnameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        a.add(fullnameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        a.add(useridLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        a.add(useridField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        a.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        a.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(updateButton);
        buttonPanel.add(backButton);

        a.add(buttonPanel, gbc);

        // Set action listeners
        updateButton.addActionListener(this);
        backButton.addActionListener(this);

        a.setVisible(true);
    }

    public void showPage() {
        a.setVisible(true); // Make the frame visible
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton) {
            // Update the admin profile
            String currentUserid = admin.getUserid(); // Store original UserID
            String newFullname = fullnameField.getText().trim();
            String newUserid = useridField.getText().trim();
            String newPassword = new String(((JPasswordField) passwordField).getPassword()).trim(); // Get password from JPasswordField

            // Check if the new User ID is already taken
            if (!newUserid.equals(currentUserid) && DataIO.checkUserid(newUserid) != null) {
                JOptionPane.showMessageDialog(a, "Username is already taken!");
                return;
            }

            // Update Fullname and Password first
            admin.setFullname(newFullname);
            DataIO.updateAdminFullname(currentUserid, newFullname);
            DataIO.updateUserFullname(currentUserid, newFullname);
            
            admin.setPassword(newPassword);
            DataIO.updateAdminPassword(currentUserid, newPassword);
            DataIO.updateUserPassword(currentUserid, newPassword);

            // Update the UserID
            if (!newUserid.equals(currentUserid)) {
                admin.setUserid(newUserid);
                DataIO.updateUserUserid(currentUserid, newUserid);
                DataIO.updateAdminUserid(currentUserid, newUserid);
            }

            // Save changes to files
            DataIO.write();

            JOptionPane.showMessageDialog(a, "Profile updated successfully.");
        } else if (e.getSource() == backButton) {
            a.setVisible(false);
            adminPage.a.setVisible(true);
        }
    }
}
