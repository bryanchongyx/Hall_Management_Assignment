public class User {
    private String userid;
    private String password;
    private String roles;
    private String fullname;

    // Constructor
    public User(String fullname, String userid, String password, String roles) {
        this.fullname = fullname;
        this.userid = userid;
        this.password = password;
        this.roles = roles;
    }

    // Getter and Setter for fullname
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    // Getter and Setter for userid
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    // Getter and Setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and Setter for roles
    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
