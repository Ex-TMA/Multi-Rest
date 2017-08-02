package gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.security.auth.login.CredentialExpiredException;
import java.io.IOException;

/**
 * Created by truongnguyen on 8/1/17.
 */
@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler implements ResponseErrorHandler{

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

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        logger.error("Response error: {} {}", response.getStatusCode(), response.getStatusText());
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return isError(response.getStatusCode());
    }


    public static boolean isError(HttpStatus status) {
        HttpStatus.Series series = status.series();
        return (HttpStatus.Series.CLIENT_ERROR.equals(series)
                || HttpStatus.Series.SERVER_ERROR.equals(series));
    }
}
