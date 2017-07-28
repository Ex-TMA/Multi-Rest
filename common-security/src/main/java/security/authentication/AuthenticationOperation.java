package security.authentication;

/**
 * Created by nsonanh on 29/07/2017.
 */
public class AuthenticationOperation implements Authentication {
    @Override
    public boolean authenticate(String userName, String pass) {
        return false;
    }
}
