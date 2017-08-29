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

    private int totpKey;

    private String secret;

    public AccountCredential() {
    }

    public AccountCredential(String userName, String pass)
    {
        this.userName = userName;
        this.pass = pass;
    }

    public AccountCredential(String userName, String pass, String secret)
    {
        this(userName, pass);
        this.secret = secret;
    }

    public AccountCredential(String userName, String pass, int totpKey)
    {
        this(userName, pass);
        this.totpKey = totpKey;
    }

    public String getUserName() {
        return userName;
    }

    public String getPass() {
        return pass;
    }

    public String getSecret() {
        return secret;
    }

    public int getTotpKey() {
        return totpKey;
    }
}
