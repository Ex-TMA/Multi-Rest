package security.service;

import org.apache.commons.codec.binary.Base32;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit4.SpringRunner;
import security.model.AccountCredential;
import security.model.AuthenticationAccount;
import security.totp.TOTPAuthenticator;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * Created by nsonanh on 12/09/2017
 */
@RunWith(SpringRunner.class)
public class TOTPAuthenticationServiceTest {

    private TOTPAuthenticationService service;

    private TOTPAuthenticator authenticator;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @MockBean
    AccountCredential accountCredential;

    @MockBean
    AuthenticationAccount authenticationAccount;

    @Before
    public void setup() {
        service = new TOTPAuthenticationService();
        authenticator = new TOTPAuthenticator();
    }

    @Test
    public void testTOTPAuthenticationFailedWhenOTPNotCorrect() throws Exception {
        thrown.expect(BadCredentialsException.class);
        thrown.expectMessage("Invalid TOTP code");

        int totpKey = 123456;
        when(accountCredential.getTotpKey()).thenReturn(totpKey);
        when(authenticationAccount.getSecret()).thenReturn("H3LFYHVBLEVX54N4");

        service.authenticateTOTPUser(accountCredential, authenticationAccount);
    }

    @Test
    public void testTOTPAuthenticationSuccessWhenOTPCorrect() throws NoSuchAlgorithmException, InvalidKeyException {
        String secret = "H3LFYHVBLEVX54N4";
        int totpKey = (int) generateTOTPKey(secret);

        when(accountCredential.getTotpKey()).thenReturn(totpKey);
        when(authenticationAccount.getSecret()).thenReturn(secret);

        try {
            service.authenticateTOTPUser(accountCredential, authenticationAccount);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    private long generateTOTPKey(String secret) throws InvalidKeyException, NoSuchAlgorithmException {
        long timeIndex = System.currentTimeMillis() / 1000 / 30;
        byte[] secretBytes = new Base32().decode(secret);
        return authenticator.getCode(secretBytes, timeIndex);
    }
}
