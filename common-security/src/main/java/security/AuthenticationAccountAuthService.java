package security;

import org.springframework.web.client.RestTemplate;
import security.model.AuthenticationAccount;

/**
 * Created by nsonanh on 29/07/2017.
 */
public class AuthenticationAccountAuthService {
    private static final String ACCOUNT_URI = "localhost:8081/account";

    public Boolean authenticateAccount(String userName, String pass)
    {
        AuthenticationAccount account = getAuthenticationAccountFromAccountService(userName);
        String encodedPass = Utils.encode(pass);

        // TODO possibly add and assign token here

        return account.getPass().equals(encodedPass);
    }

    private AuthenticationAccount getAuthenticationAccountFromAccountService(String userName)
    {
        RestTemplate restTemplateToAccount = new RestTemplate();
        String exactURI = ACCOUNT_URI + "/search/findByUserName?userName=" + userName + "&projection=authenticateAccount";
        AuthenticationAccount resultAccount = restTemplateToAccount.getForObject(ACCOUNT_URI, AuthenticationAccount.class);
        return resultAccount;
    }
}
