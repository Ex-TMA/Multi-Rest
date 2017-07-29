package security.model;

/**
 * Created by nsonanh on 29/07/2017.
 */
public class AuthenticationAccount {
    private String userName;
    private String pass;

    protected AuthenticationAccount() {
    }

    public String getUserName() {
        return userName;
    }

    public String getPass() {
        return pass;
    }
}