package security;

import config.RequestHeader;
import config.property.ConfigProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import security.exception.MissingTOTPKeyAuthenticatorException;
import security.model.AccountCredential;
import security.model.AccountToken;
import security.model.AuthenticationAccount;
import security.repository.AccountTokenRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by nsonanh on 02/08/2017.
 */
@Service
public class AccountAuthenticationService {

    private Logger logger = LoggerFactory.getLogger(AccountAuthenticationService.class);

    @Autowired
    private ConfigProperties configProperties;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AccountTokenRepository accountTokenRepository;

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

        return new AuthenticationAccount(respEntity.getBody().getUserName(), respEntity.getBody().getPass(), respEntity.getBody().getSecret());
    }

    public void saveAccountToken(String userName, String token, String ip){
        LocalDate expiresAt = LocalDate.now().plusDays(1);
        AccountToken accountToken = new AccountToken(userName, token, ip, java.sql.Date.valueOf(expiresAt));
        accountToken = accountTokenRepository.save(accountToken);
        logger.info("Account save to DB - " + accountToken.toString());
    }

    public boolean isTokenAndIpFound(String token, String ip){
        return accountTokenRepository.findByTokenAndIpAndExpiresAtAfter(token, ip, new Date()) != null;
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

    public String generateOTPProtocol(AccountCredential request) {
        AuthenticationAccount account = getAuthenticationAccountFromAccountService(request.getUserName());

        if (account == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        if (!matchPassword(request.getPass(), account.getPassword())) {
            throw new AuthenticationCredentialsNotFoundException("Authentication failed");
        }

        if (StringUtils.isEmpty(account.getSecret())) {
            throw new MissingTOTPKeyAuthenticatorException("No secret key provided");
        }
        return String.format("otpauth://totp/%s?secret=%s&issuer=SpringBootTOTP", account.getUsername(), account.getSecret());
    }
}
