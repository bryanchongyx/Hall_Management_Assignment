import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class PageSuperAdmin implements ActionListener {
    JFrame a;
    JButton manage, edit, logout;
    User user;
    
    public PageSuperAdmin(User user) {
        this.user = user;
        a = new JFrame();
        a.setTitle("Create & Delete Administrator");
        a.setSize(300, 300);
        a.setLocation(500, 325);
        a.setLayout (new FlowLayout());

        // Buttons
        manage = new JButton("Manage Admin");
        edit = new JButton ("Edit Profile");
        logout = new JButton("Logout");

        
        manage.addActionListener(this);
        logout.addActionListener(this);
        edit.addActionListener (this);
        
        a.add (manage);
        a.add (edit);
        a.add (logout);
        
        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == manage) {
                a.setVisible(false);
                if (Main.a9 == null){
                    Main.a11 = new PageAdminManagement (this);
                }
                Main.a11.showPage();
            }
            else if (e.getSource() == edit){
                
                //prompt new details
                String newFullname = JOptionPane.showInputDialog ("Edit Full Name:", user.getFullname()).trim();
                if (newFullname != null && !newFullname.isEmpty()){
                    user.setFullname (newFullname);
                    DataIO.updateUserFullname (user.getUserid(), newFullname);
                }
                
                String newUserid = JOptionPane.showInputDialog("Edit User ID:", user.getUserid()).trim();
                if (newUserid != null && !newUserid.isEmpty() && !newUserid.equals (user.getUserid())){
                DataIO.updateUserUserid (user.getUserid(), newUserid);
                user.setUserid (newUserid);
            }
                else {
                    JOptionPane.showMessageDialog (a, "User ID is already taken!");
                    return;
                }
                String newPassword = JOptionPane.showInputDialog ("Edit Password:", user.getPassword()).trim();
                if (newPassword != null && !newPassword.isEmpty()){
                    user.setPassword (newPassword);
                    DataIO.updateUserPassword (user.getUserid(), newPassword);
                    
                }
                
                //Save data 
                DataIO.write();
                JOptionPane.showMessageDialog (a, "Super Admin details updated succesfully");
            }
            
            else if  (e.getSource()== logout){
                a.setVisible(false);
                Main.a1.a.setVisible (true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(a, ex.getMessage());
        }
    }
}
