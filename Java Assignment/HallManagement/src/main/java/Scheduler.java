public class Scheduler {
    private String fullname;
    private String userid;
    private String password;
    private String joinedDate;

    public Scheduler(String fullname, String userid, String password, String joinedDate) {
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

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setJoinedDate(String joinedDate) {
        this.joinedDate = joinedDate;
    }

    public String getUserid() {
        return userid;
    }

    public String getPassword() {
        return password;
    }

    public String getJoinedDate() {
        return joinedDate;
    }
}
