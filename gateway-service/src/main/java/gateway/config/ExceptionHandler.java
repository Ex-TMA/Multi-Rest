package gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.security.auth.login.CredentialExpiredException;

/**
 * Created by truongnguyen on 8/1/17.
 */
@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @org.springframework.web.bind.annotation.ExceptionHandler({Throwable.class})
    protected ResponseEntity<Object> handleThrowable(Exception e, WebRequest request) {
        String body = e.getMessage();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if( e instanceof UsernameNotFoundException ){
            logger.error("UsernameNotFound error={}, reason={}", e.getClass().getSimpleName(), e.getMessage(), e);
            status = HttpStatus.NOT_FOUND;
        }
        if( e instanceof AuthenticationCredentialsNotFoundException){
            logger.error("AuthenticationCredentialsNotFoundException error={}, reason={}", e.getClass().getSimpleName(), e.getMessage(), e);
            status = HttpStatus.UNAUTHORIZED;
        }
        if(e instanceof CredentialExpiredException){
            logger.error("CredentialExpiredException error={}, reason={}", e.getClass().getSimpleName(), e.getMessage(), e);
            status = HttpStatus.UNAUTHORIZED;
        }

        logger.error("Unhandled error={}, reason={}", e.getClass().getSimpleName(), e.getMessage(), e);
        return handleExceptionInternal(e, body, new HttpHeaders(), status, request);
    }
}
