
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;


public class PageAdmin implements ActionListener {
    JFrame a;
    Button scheduler, customer, booking, logout, manager,edit;

    
    public PageAdmin (){
        a = new JFrame();
        a.setTitle ("Admin Page");
        a.setSize (400, 200);
        a.setLocation (575, 375);
        a.setLayout (new FlowLayout());
        
        scheduler = new Button ("Scheduler Staff Management");
        customer = new Button ("Customer Management");
        booking = new Button ("Booking Management");
        logout = new Button ("Log Out");
        manager = new Button ("Manager Staff Management");
        edit = new Button ("Edit Own Profile");
        
        scheduler.addActionListener (this);
        customer.addActionListener (this);
        booking.addActionListener (this);
        logout.addActionListener (this);
        manager.addActionListener(this);
        edit.addActionListener(this);
        
        a.add(scheduler);
        a.add(customer);
        a.add (booking);
        a.add (manager);
        a.add (edit);
        a.add (logout);
        
        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }    
    public void actionPerformed (ActionEvent e){
        if (e.getSource()==scheduler){
            a.setVisible(false);
            if (Main.a5 == null){
                Main.a5 = new PageSchedulerManagement(this);
            }
            Main.a5.showPage();
            
        }
        else if (e.getSource()==customer){
            a.setVisible(false);
            if (Main.a6 == null){
                Main.a6 = new PageCustomerManagement(this);
            }
            Main.a6.showPage();
        }
        
        else if (e.getSource()==manager){
            a.setVisible (false);
            if (Main.a7 == null){
                Main.a7 = new PageManagerManagement(this); 
            }
            Main.a7.showPage();
        }
        else if (e.getSource() == booking){
            JOptionPane.showMessageDialog (a,"Not Ready Yet");
        }
        else if (e.getSource () == edit){
            a.setVisible(false);
            
        }
        else if (e.getSource()==logout){
            a.setVisible (false);
            Main.a1.a.setVisible(true);
        }
        

    }
}
