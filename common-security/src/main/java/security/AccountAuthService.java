package security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import security.model.AuthAcc;
import security.model.AuthenticationAccount;

/**
 * Created by nsonanh on 02/08/2017.
 */
public class AccountAuthService {
    @Autowired
    SecurityProperties securityProperties;

    private static final String ACCOUNT_URI = "http://localhost:8081/api/accounts";
    private PasswordEncoder encoder;

    public AccountAuthService() {
    }

    public AuthenticationAccount authenticateAccount(AuthAcc request) {
        AuthenticationAccount account = getAuthenticationAccountFromAccountService(request.getUserName());

        if (account == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        if (!matchPassword(request.getPass(), account.getPassword())) {
            throw new AuthenticationCredentialsNotFoundException("Authentication failed");
        }
        return account;
    }

    public AuthenticationAccount getAuthenticationAccountFromAccountService(String userName) {
        RestTemplate restTemplateToAccount = new RestTemplate();
        String exactURI = ACCOUNT_URI + "/search/findByUserName?userName=" + userName + "&projection=authenticateAccount";
        AuthAcc resultAccount = restTemplateToAccount.getForObject(exactURI, AuthAcc.class);
        return new AuthenticationAccount(resultAccount.getUserName(), resultAccount.getPass());
    }

    private boolean matchPassword(String rawPass, String encodedPass) {
        try {
            // Encoder
            Class<?> encoderClass = Class.forName(securityProperties.getEncryptionMethodClass());
            encoder = (PasswordEncoder) encoderClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return encoder != null ? encoder.matches(rawPass, encodedPass) : false;
    }
}
