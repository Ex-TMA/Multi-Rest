package security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import security.AccountAuthenticationService;
import security.TOTPAuthenticationService;
import security.exception.MissingTOTPKeyAuthenticatorException;
import security.model.AccountCredential;
import security.model.AuthenticationAccount;
import security.model.ErrorResponse;

import javax.security.auth.login.CredentialExpiredException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by nsonanh on 02/08/2017.
 */
@RestController
@Validated
@RequestMapping("/api")
public class SecurityController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private AccountAuthenticationService customerAuthService;

    @Autowired
    private TOTPAuthenticationService totpAuthenticationService;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/login", method = POST)
    public ResponseEntity<AuthenticationAccount> doAuth(HttpServletRequest servletRequest, @RequestBody @Valid AccountCredential request) throws IOException {

        AuthenticationAccount customer = customerAuthService.authenticateAccount(request);

        totpAuthenticationService.authenticateTOTPUser(request, customer);

        Token token = tokenService.allocateToken(objectMapper.writeValueAsString(customer));
        tokenService.verifyToken(token.getKey());
        customerAuthService.saveAccountToken(customer.getUsername(),token.getKey(), servletRequest.getRemoteHost());

        return ResponseEntity.status(HttpStatus.OK).header(config.RequestHeader.ACCESS_TOKEN, token.getKey())
                .body(customer);
    }

    @RequestMapping(value = "/user", method = GET)
    public ResponseEntity<AuthenticationAccount> getCurrentUser(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.status(HttpStatus.OK).body(customerAuthService.getAuthenticationAccountFromAccountService(auth.getName()));
    }

    @RequestMapping(value = "/qrcode", method = RequestMethod.POST)
    public void generateQRCode(HttpServletResponse response, @RequestBody @Valid AccountCredential request) throws WriterException, IOException {
        String otpProtocol = customerAuthService.generateOTPProtocol(request);
        response.setContentType("image/png");
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(otpProtocol, BarcodeFormat.QR_CODE, 250, 250);
        MatrixToImageWriter.writeToStream(matrix, "PNG", response.getOutputStream());
        response.getOutputStream().flush();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UsernameNotFoundException.class})
    public ErrorResponse userNameNotFound() {
        return new ErrorResponse("Username not found");
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({AuthenticationException.class})
    public ErrorResponse authenticationFailed() {
        return new ErrorResponse("Authentication failed");
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({CredentialExpiredException.class})
    public ErrorResponse credentialExpired() {
        return new ErrorResponse("User credentials expired");
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({BadCredentialsException.class})
    public ErrorResponse badCredentials() {
        return new ErrorResponse("Bad credentials provided");
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({IOException.class, InternalAuthenticationServiceException.class})
    public ErrorResponse internalAuthenticationException() {
        return new ErrorResponse("TOTP code verification failed");
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MissingTOTPKeyAuthenticatorException.class})
    public ErrorResponse missingTOTPCode() {
        return new ErrorResponse("TOTP code is mandatory");
    }
}
