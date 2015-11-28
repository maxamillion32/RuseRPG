package space.nthompson.ruserpg.Activities.Activities;

/**
 * Created by Nick on 11/27/15.
 */
public class User {
    private String userID;
    private String name;
    private String email;

    public User(String userTwitterID, String userName, String userEmail) {

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void String(String userID) {
        this.userID = userID;
    }
}
