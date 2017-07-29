package security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;
import security.model.AuthenticationAccount;

import java.io.IOException;

/**
 * Created by nsonanh on 29/07/2017.
 */
public class AuthenticationAccountAuthService {
    private static final String ACCOUNT_URI = "localhost:8081/accounts";

    public ResponseEntity authenticateAccount(AuthenticationAccount request) {
        AuthenticationAccount account = getAuthenticationAccountFromAccountService(request.getUserName());

        if (account == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        if (!Utils.match(request.getPass(), account.getPass())) {
            throw new AuthenticationCredentialsNotFoundException("Authentication failed");
        }

        Token token = Utils.generateToken(account.getUserName());
        return ResponseEntity.status(HttpStatus.OK).header("access-token", token.getKey())
                .body(account.getUserName());
    }

    public Boolean authenticateToken(String tokenString) throws IOException {
        if (tokenString == null) {
            return false;
        }
        Token token = Utils.verifyToken(tokenString);
        String userName = token.getExtendedInformation();

        return userName != null && (System.currentTimeMillis() - token.getKeyCreationTime()) < Utils.DAY;
    }

    private AuthenticationAccount getAuthenticationAccountFromAccountService(String userName)
    {
        RestTemplate restTemplateToAccount = new RestTemplate();
        String exactURI = ACCOUNT_URI + "/search/findByUserName?userName=" + userName + "&projection=authenticateAccount";
        AuthenticationAccount resultAccount = restTemplateToAccount.getForObject(exactURI, AuthenticationAccount.class);
        return resultAccount;
    }
}
