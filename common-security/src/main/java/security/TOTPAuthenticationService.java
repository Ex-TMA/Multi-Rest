package security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.util.StringUtils;
import security.exception.MissingTOTPKeyAuthenticatorException;
import security.model.AccountCredential;
import security.model.AuthenticationAccount;
import security.totp.TOTPAuthenticator;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by nsonanh on 29/08/2017
 */
public class TOTPAuthenticationService {
    private final Logger logger = LoggerFactory.getLogger(TOTPAuthenticationService.class);

    private static final TOTPAuthenticator totpAuthenticator;

    static {
        totpAuthenticator = new TOTPAuthenticator();
    }

    public void authenticateTOTPUser(AccountCredential request, AuthenticationAccount account) {
        String secret = account.getSecret();

        if (StringUtils.hasText(secret)) {
            Integer totpKey = request.getTotpKey();
            if (totpKey != null) {
                try {
                    if (!totpAuthenticator.verifyCode(secret, totpKey, 2)) {
                        logger.info("Code {} was not valid", totpKey);
                        System.out.printf("Code %d was not valid", totpKey);
                        throw new BadCredentialsException(
                                "Invalid TOTP code");
                    }
                }
                catch (InvalidKeyException | NoSuchAlgorithmException e) {
                    throw new InternalAuthenticationServiceException(
                            "TOTP code verification failed", e);
                }

            }
            else {
                throw new MissingTOTPKeyAuthenticatorException(
                        "TOTP code is mandatory");
            }
        }
    }
}
