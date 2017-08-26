package security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Service;
import security.exception.TokenAndIpNotFoundException;
import security.model.AuthenticationAccount;
import security.model.AuthenticationUser;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by nsonanh on 02/08/2017.
 */
@Service
public class TokenAuthenticationService {
    private Logger logger = LoggerFactory.getLogger(TokenAuthenticationService.class);
    private static final long DAY = 1000 * 60 * 60 * 24;
    protected static final String TOKEN_HEADER = "access-token";

    private TokenService tokenService;

    @Autowired
    private AccountAuthenticationService accountAuthenticationService;

    private ObjectMapper mapper = new ObjectMapper();

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public Authentication doAuthentication(HttpServletRequest request) throws IOException {
        final String tokenString = request.getHeader(TOKEN_HEADER);

        if (tokenString != null) {
            Token token = this.tokenService.verifyToken(tokenString);
            if(!accountAuthenticationService.isTokenAndIpFound(token.getKey(), request.getRemoteHost())){
                throw new TokenAndIpNotFoundException("Token and IP not found");
            }
            logger.info("Pass Authentication on token and ip");
            final AuthenticationAccount acc = this.mapper.readValue(token.getExtendedInformation(), AuthenticationAccount.class);

            if (acc != null && (System.currentTimeMillis() - token.getKeyCreationTime()) < DAY) {
                return new AuthenticationUser(acc);
            }
        }
        return null;
    }
}
