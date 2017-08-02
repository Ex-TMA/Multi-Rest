package security.model;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by nsonanh on 02/08/2017.
 */
public class AuthRequest {
    @NotBlank
    private String userName;

    @NotBlank
    private String password;

    public AuthRequest() {
    }

    public AuthRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
