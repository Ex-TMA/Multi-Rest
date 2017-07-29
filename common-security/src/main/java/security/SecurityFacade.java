package security;

import security.authentication.Authentication;
import security.authentication.AuthenticationOperation;
import security.token_management.TokenManagement;
import security.token_management.TokenManagementOperation;

/**
 * Created by nsonanh on 29/07/2017.
 */
public class SecurityFacade {
    private Authentication authenticationOperation;
    private TokenManagement tokenManagementOperation;

    public SecurityFacade()
    {
        this.authenticationOperation = new AuthenticationOperation();
        this.tokenManagementOperation = new TokenManagementOperation();
    }

    public Boolean performAuthenticate(String userName, String pass)
    {
        return authenticationOperation.authenticate(userName, pass);
    }

    public Boolean performTokenVerification(String token)
    {
        return tokenManagementOperation.verifyToken(token);
    }
}
