package security.authentication;

/**
 * Created by nsonanh on 29/07/2017.
 */
public interface Authentication {
    boolean authenticate(String userName, String pass);
}
