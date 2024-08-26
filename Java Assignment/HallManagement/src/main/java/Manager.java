
public class Manager {
    private String fullname;
    private String userid;
    private String password;
    private String joinedDate;
    
    public Manager (String fullname, String userid, String password, String joinedDate){
        this.fullname = fullname;
        this.userid = userid;
        this.password = password;
        this.joinedDate = joinedDate;
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

    public String getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(String joinedDate) {
        this.joinedDate = joinedDate;
    }
}
