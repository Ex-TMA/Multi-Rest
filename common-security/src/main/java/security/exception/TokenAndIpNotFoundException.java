package security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by truongnguyen on 8/22/17.
 */
public class TokenAndIpNotFoundException extends AuthenticationException {

    public TokenAndIpNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public TokenAndIpNotFoundException(String msg) {
        super(msg);
    }
}
