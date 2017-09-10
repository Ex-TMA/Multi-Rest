package security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.DefaultToken;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.token.TokenService;
import org.springframework.test.context.junit4.SpringRunner;
import security.exception.TokenAndIpNotFoundException;
import security.model.AuthenticationAccount;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TokenAuthenticationServiceTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private TokenAuthenticationService service;

	@MockBean
	private AccountAuthenticationService accountAuthenticationService;

	@MockBean TokenService tokenService;

	@MockBean HttpServletRequest request;

	@Before
	public void setup() throws Exception{
		service = new TokenAuthenticationService(tokenService, accountAuthenticationService);
	}

	@Test
	public void whenTokenAndIpNotFoundShouldThrowException()throws Exception{
		thrown.expect(TokenAndIpNotFoundException.class);
		thrown.expectMessage("Token and IP not found");

		String tokenString = "test-token-1234566788ABC";
		String extendedInformation ="testuser-testpass";
		Token testToken = new DefaultToken("test-01234566", 1234556L,extendedInformation);
		when(request.getHeader(eq("access-token"))).thenReturn(tokenString);
		when(tokenService.verifyToken(eq(tokenString))).thenReturn(testToken);
		when(accountAuthenticationService.isTokenAndIpFound(anyString(),anyString())).thenReturn(false);

		service.doAuthentication(request);
	}

	@Test
	public void getAuthenticationOkWhenTokenAndIpPassVerification()throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		String tokenString = "test-token-1234566788ABC";
		AuthenticationAccount extendedInformation = new AuthenticationAccount("test-user", "test-pass");
		Token testToken = new DefaultToken("test-01234566", new Date().getTime(),mapper.writeValueAsString(extendedInformation));
		when(request.getHeader(eq("access-token"))).thenReturn(tokenString);
		when(tokenService.verifyToken(eq(tokenString))).thenReturn(testToken);
		when(accountAuthenticationService.isTokenAndIpFound(anyString(),anyString())).thenReturn(true);

		Authentication authentication = service.doAuthentication(request);
		assertThat(authentication.getName()).isEqualTo(extendedInformation.getUsername());
	}
}