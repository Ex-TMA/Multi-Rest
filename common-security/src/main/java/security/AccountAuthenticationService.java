package security;

import config.RequestHeader;
import config.property.ConfigProperties;
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
    private ConfigProperties configProperties;

    @Autowired
    private PasswordEncoder encoder;

    private static final String ACCOUNT_URI = "http://localhost:8081/api/accounts";
    private static final String FIND_BY_USERNAME = "/search/findByUserName?userName=";
    private static final String PROJECTION = "&projection=authenticateAccount";

    private String gatewayPasskeyContent;

    public AuthenticationAccount authenticateAccount(AccountCredential request) {
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
        headers.set(RequestHeader.GATEWAY_PASSKEY, getGatewayPasskeyContent());

        HttpEntity<String> entity = new HttpEntity<String>("", headers);

        String exactURI = ACCOUNT_URI + FIND_BY_USERNAME + userName + PROJECTION;
        ResponseEntity<AccountCredential> respEntity = restTemplateToAccount.exchange(exactURI, HttpMethod.GET, entity, AccountCredential.class);

        return new AuthenticationAccount(respEntity.getBody().getUserName(), respEntity.getBody().getPass());
    }

    private boolean matchPassword(String rawPass, String encodedPass) {
        return encoder.matches(rawPass, encodedPass);
    }

    private String getGatewayPasskeyContent() {
        if (gatewayPasskeyContent == null) {
            gatewayPasskeyContent = encoder.encode(configProperties.getGatewayPasskey());
        }
        return gatewayPasskeyContent;
    }
}
