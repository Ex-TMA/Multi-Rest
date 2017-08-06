package security.model;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by nsonanh on 03/08/2017.
 */
public class AccountCredential {
    @NotBlank
    private String userName;
    @NotBlank
    private String pass;

    public AccountCredential() {
    }

    public AccountCredential(String userName, String pass)
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
