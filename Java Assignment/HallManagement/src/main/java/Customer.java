
public class Customer {
    private String fullname;
    private String userid;
    private String password;
    private String email;
    private String ph_number;
    
    public Customer (String fullname, String userid, String password, String email, String ph_number){
        this.fullname = fullname;
        this.userid = userid;
        this.password = password;
        this.email = email;
        this.ph_number = ph_number;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPh_number() {
        return ph_number;
    }

    public void setPh_number(String ph_number) {
        this.ph_number = ph_number;
    }
    
    
}
