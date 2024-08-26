
public class Customer {
    private String userid;
    private String password;
    private String fullname;
    private String email;
    private String ph_number;
    
    public Customer (String userid, String password, String fullname, String email, String ph_number){
        this.userid = userid;
        this.password = password;
        this.fullname= fullname;
        this.email = email;
        this.ph_number = ph_number;
    }
    
    public String getPh_number() {
        return ph_number;
    }

    public void setPh_number(String ph_number) {
        this.ph_number = ph_number;
    }
   

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    
    
}
