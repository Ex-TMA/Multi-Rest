package security.service;

import config.RequestHeader;
import config.property.ConfigProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
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

    private ConfigProperties configProperties;

    private PasswordEncoder encoder;

    private AccountTokenRepository accountTokenRepository;

    RestTemplate restTemplateToAccount = null;

    public static final String ACCOUNT_URI = "http://localhost:8081/api/accounts";
    public static final String FIND_BY_USERNAME = "/search/findByUserName?userName=";
    public static final String PROJECTION = "&projection=authenticateAccount";

    private String gatewayPasskeyContent;

    @Autowired
    public AccountAuthenticationService(ConfigProperties configProperties, PasswordEncoder encoder, AccountTokenRepository accountTokenRepository, RestTemplateBuilder restTemplateBuilder){
        this.configProperties = configProperties;
        this.encoder = encoder;
        this.accountTokenRepository = accountTokenRepository;
        this.restTemplateToAccount = restTemplateBuilder.build();
    }
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
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(RequestHeader.GATEWAY_PASSKEY, getGatewayPasskeyContent());

        HttpEntity<String> entity = new HttpEntity<String>("", headers);

        String exactURI = ACCOUNT_URI + FIND_BY_USERNAME + userName + PROJECTION;
        try{
            ResponseEntity<AccountCredential> respEntity = restTemplateToAccount.exchange(exactURI, HttpMethod.GET, entity, AccountCredential.class);
            return new AuthenticationAccount(respEntity.getBody().getUserName(), respEntity.getBody().getPass(), respEntity.getBody().getSecret());
        }
        catch (RestClientResponseException ex){
            logger.warn("RestClientException status: %s, reason: %s", ex.getRawStatusCode(), ex.getStatusText());
        }
        return null;
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
