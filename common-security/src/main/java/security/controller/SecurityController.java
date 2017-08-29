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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.token.TokenService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import security.AccountAuthenticationService;
import security.TOTPAuthenticationService;
import security.model.AccountCredential;
import security.model.AuthenticationAccount;
import security.model.ErrorResponse;

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

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({Exception.class})
    public ErrorResponse customersNotFound() {
        return new ErrorResponse("Customer not found");
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
}
