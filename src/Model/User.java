package Model;

public class User {
    private int userID;
    private String userName;
    private String password;

    /**constructor for the customer class*/
    public User(int userID, String userName, String password) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
    }
    /**@return userId*/
    public int getUserID() {
        return userID;
    }
    /**set userId*/
    public void setUserID(int userID) {
        this.userID = userID;
    }
    /**@return username*/
    public String getUserName() {
        return userName;
    }
    /**setusername*/
    public void setUserName(String userName) {
        this.userName = userName;
    }
    /**@return password*/
    public String getPassword() {
        return password;
    }
    /**set password*/
    public void setPassword(String password) {
        this.password = password;
    }
}
