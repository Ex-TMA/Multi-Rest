package security.model;

/**
 * Created by nsonanh on 03/08/2017.
 */
public class AuthAcc {
    private String userName;
    private String pass;

    public AuthAcc() {
    }

    public AuthAcc(String userName, String pass)
    {
        this.userName = userName;
        this.pass = pass;
    }

    public String getUserName() {
        return userName;
    }

    public String getPass() {
        return pass;
    }
}
