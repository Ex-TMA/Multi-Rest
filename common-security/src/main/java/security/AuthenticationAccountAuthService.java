package security;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;
import security.model.AuthenticationAccount;

/**
 * Created by nsonanh on 29/07/2017.
 */
public class AuthenticationAccountAuthService {
    private static final String ACCOUNT_URI = "localhost:8081/account";

    public AuthenticationAccount authenticateAccount(String userName, String pass) {
        AuthenticationAccount account = getAuthenticationAccountFromAccountService(userName);

        if (account == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        if (!Utils.match(pass, account.getPass())) {
            throw new AuthenticationCredentialsNotFoundException("Authentication failed");
        }

        // TODO generate & assign token

        return account;
    }

    private AuthenticationAccount getAuthenticationAccountFromAccountService(String userName)
    {
        RestTemplate restTemplateToAccount = new RestTemplate();
        String exactURI = ACCOUNT_URI + "/search/findByUserName?userName=" + userName + "&projection=authenticateAccount";
        AuthenticationAccount resultAccount = restTemplateToAccount.getForObject(exactURI, AuthenticationAccount.class);
        return resultAccount;
    }
}
