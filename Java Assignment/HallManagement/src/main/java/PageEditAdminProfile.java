
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class PageEditAdminProfile implements ActionListener {
    JFrame a;
    JTextField fullnameField, useridField, passwordField;
    JButton updateButton, backButton;
    Admin admin;

    public PageEditAdminProfile(Admin admin) {
        this.admin = admin;
        a = new JFrame();
        a.setTitle("Edit Admin Profile");
        a.setLocation (625,350);
        a.setSize(300, 200);
        a.setLayout(new FlowLayout());
        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fullnameField = new JTextField(admin.getFullname(), 20);
        useridField = new JTextField(admin.getUserid(), 20);
        passwordField = new JTextField(admin.getPassword(), 20);

        updateButton = new JButton("Update");
        backButton = new JButton("Back");

        updateButton.addActionListener(this);
        backButton.addActionListener(this);

        a.add(new JLabel("Full Name:"));
        a.add(fullnameField);
        a.add(new JLabel("User ID:"));
        a.add(useridField);
        a.add(new JLabel("Password:"));
        a.add(passwordField);
        a.add(updateButton);
        a.add(backButton);
        a.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton) {
            // Update the admin profile
            String currentUserid = admin.getUserid(); // Store original UserID
            String newFullname = fullnameField.getText().trim();
            String newUserid = useridField.getText().trim();
            String newPassword = passwordField.getText().trim();

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

            //update the UserID
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
            new PageAdmin(admin).a.setVisible(true);
        }
    }
}



