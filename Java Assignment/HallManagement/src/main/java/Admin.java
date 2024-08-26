
public class Admin {
    private String fullname;
    private String userid;
    private String password;
    private String joined_date;
    
    public Admin (String fullname, String userid, String password, String joined_date){
        this.fullname = fullname;
        this.userid = userid;
        this.password = password;
        this.joined_date = joined_date;
    }

    public String getDate() {
        return joined_date;
    }

    public void setDate(String joined_date) {
        this.joined_date = joined_date;
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
    
        
}
