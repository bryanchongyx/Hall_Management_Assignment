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

public class PageAdmin implements ActionListener {
    JFrame a;
    JButton scheduler, customer, booking, logout, manager, edit;
    Admin admin;

    public PageAdmin(Admin admin) {
        this.admin = admin;
        a = new JFrame("Admin Dashboard");
        a.setSize(600, 400);
        a.setLocationRelativeTo(null); // Center the frame
        a.setLayout(new GridBagLayout());
        a.getContentPane().setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        // Title Label
        JLabel title = new JLabel("Admin Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        a.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;

        // Create and add buttons
        scheduler = createButton("Scheduler Staff Management");
        customer = createButton("Customer Management");
        booking = createButton("Booking Management");
        manager = createButton("Manager Staff Management");
        edit = createButton("Edit Own Profile");
        logout = createButton("Log Out");

        gbc.gridx = 0;
        a.add(scheduler, gbc);

        gbc.gridx = 1;
        a.add(customer, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        a.add(booking, gbc);

        gbc.gridx = 1;
        a.add(manager, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        a.add(edit, gbc);

        gbc.gridx = 1;
        a.add(logout, gbc);

        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(250, 50));
        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(Color.DARK_GRAY);
        button.addActionListener(this);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == scheduler) {
            a.setVisible(false);
            if (Main.a5 == null) {
                Main.a5 = new PageSchedulerManagement(this);
            }
            Main.a5.showPage();
        } else if (e.getSource() == customer) {
            a.setVisible(false);
            if (Main.a6 == null) {
                Main.a6 = new PageCustomerManagement(this);
            }
            Main.a6.showPage();
        } else if (e.getSource() == manager) {
            a.setVisible(false);
            if (Main.a7 == null) {
                Main.a7 = new PageManagerManagement(this);
            }
            Main.a7.showPage();
        } else if (e.getSource() == booking) {
            JOptionPane.showMessageDialog(a, "Not Ready Yet");
        } else if (e.getSource() == edit) {
            a.setVisible(false);
            if (Main.a10 == null) {
                Main.a10 = new PageEditAdminProfile(this, admin);
            }
            Main.a10.showPage();
        } else if (e.getSource() == logout) {
            a.setVisible(false);
            Main.a1.a.setVisible(true);
        }
    }

}
