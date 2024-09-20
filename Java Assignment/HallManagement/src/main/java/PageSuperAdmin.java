import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.SwingConstants;

public class PageSuperAdmin implements ActionListener {
    JFrame a;
    JButton manage, edit, logout;
    User user;

    public PageSuperAdmin(User user) {
        this.user = user;
        a = new JFrame("Super Admin Management");
        a.setSize(400, 300);
        a.setLocationRelativeTo(null);
        a.setLayout(new BorderLayout(10, 10)); // Use BorderLayout with padding

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + user.getFullname() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setOpaque(true);
        welcomeLabel.setBackground(new Color(0x4CAF50));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setPreferredSize(new Dimension(400, 40));
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        manage = createStyledButton("Manage Admin");
        buttonPanel.add(manage, gbc);

        gbc.gridy++;
        edit = createStyledButton("Edit Profile");
        buttonPanel.add(edit, gbc);

        gbc.gridy++;
        logout = createStyledButton("Logout");
        buttonPanel.add(logout, gbc);

        // Action Listeners
        manage.addActionListener(this);
        edit.addActionListener(this);
        logout.addActionListener(this);

        // Adding panels to frame
        a.add(headerPanel, BorderLayout.NORTH);
        a.add(buttonPanel, BorderLayout.CENTER);

        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Helper method to create styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(0x2196F3));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == manage) {
                a.setVisible(false);
                if (Main.a9 == null) {
                    Main.a11 = new PageAdminManagement(this);
                }
                Main.a11.showPage();
            } else if (e.getSource() == edit) {
                editSuperAdminProfile();
            } else if (e.getSource() == logout) {
                int confirm = JOptionPane.showConfirmDialog(a, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    a.setVisible(false);
                    Main.a1.a.setVisible(true);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(a, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to edit Super Admin profile with confirmation
    private void editSuperAdminProfile() throws Exception {
        String newFullname = JOptionPane.showInputDialog(a, "Edit Full Name:", user.getFullname()).trim();
        if (newFullname != null && !newFullname.isEmpty()) {
            user.setFullname(newFullname);
            DataIO.updateUserFullname(user.getUserid(), newFullname);
        }

        String newUserid = JOptionPane.showInputDialog(a, "Edit User ID:", user.getUserid()).trim();
        if (newUserid != null && !newUserid.isEmpty() && !newUserid.equals(user.getUserid())) {
            if (DataIO.checkUserid(newUserid) == null) {
                DataIO.updateUserUserid(user.getUserid(), newUserid);
                user.setUserid(newUserid);
            } else {
                JOptionPane.showMessageDialog(a, "User ID is already taken!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String newPassword = JOptionPane.showInputDialog(a, "Edit Password:", user.getPassword()).trim();
        if (newPassword != null && !newPassword.isEmpty()) {
            user.setPassword(newPassword);
            DataIO.updateUserPassword(user.getUserid(), newPassword);
        }

        DataIO.write(); // Save data to file
        JOptionPane.showMessageDialog(a, "Super Admin details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

}
