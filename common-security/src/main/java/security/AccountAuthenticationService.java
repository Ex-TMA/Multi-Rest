package security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import security.model.AccountCredential;
import security.model.AuthenticationAccount;

import java.util.Arrays;

/**
 * Created by nsonanh on 02/08/2017.
 */
public class AccountAuthenticationService {
    @Autowired
    SecurityProperties securityProperties;

    private static final String ACCOUNT_URI = "http://localhost:8081/api/accounts";
    private PasswordEncoder encoder;

    public AuthenticationAccount authenticateAccount(AccountCredential request, String gatewayPasskey) {
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

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("gateway-passkey", "$2a$06$nnGtPHivmQRvBGtffykHAeUsRNoW9.wiUkYJgg8vIXZDQNVbHqfve");

        HttpEntity<String> entity = new HttpEntity<String>("", headers);

        String exactURI = ACCOUNT_URI + "/search/findByUserName?userName=" + userName + "&projection=authenticateAccount";
        ResponseEntity<AccountCredential> respEntity = restTemplateToAccount.exchange(exactURI, HttpMethod.GET, entity, AccountCredential.class);

        return new AuthenticationAccount(respEntity.getBody().getUserName(), respEntity.getBody().getPass());
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
