package security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by nsonanh on 28/08/2017
 */
public class MissingTOTPKeyAuthenticatorException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public MissingTOTPKeyAuthenticatorException(String msg) {
        super(msg);
    }
}
