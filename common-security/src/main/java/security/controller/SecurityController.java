package security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import security.model.AccountCredential;
import security.model.AuthenticationAccount;
import security.model.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
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

    private static ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/login", method = POST)
    public ResponseEntity<AuthenticationAccount> doAuth(@RequestBody @Valid AccountCredential request) throws IOException {

        AuthenticationAccount customer = customerAuthService.authenticateAccount(request);

        Token token = tokenService.allocateToken(objectMapper.writeValueAsString(customer));
        tokenService.verifyToken(token.getKey());
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
}
