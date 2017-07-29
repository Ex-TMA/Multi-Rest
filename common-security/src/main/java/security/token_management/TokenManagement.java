package security.token_management;

/**
 * Created by nsonanh on 29/07/2017.
 */
public interface TokenManagement {
    boolean verifyToken(String token);
}
