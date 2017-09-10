package security.service;

import config.property.ConfigProperties;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import security.exception.MissingTOTPKeyAuthenticatorException;
import security.model.AccountCredential;
import security.model.AuthenticationAccount;
import security.repository.AccountTokenRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class AccountAuthenticationServiceTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private  AccountAuthenticationService service;

	@MockBean
	private ConfigProperties configProperties;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@MockBean
	private AccountTokenRepository accountTokenRepository;

	@MockBean
	private RestTemplateBuilder restTemplateBuilder;

	@MockBean
	private RestTemplate restTemplate;

	@Before
	public void setup() throws Exception {
		when(restTemplateBuilder.build()).thenReturn(restTemplate);
		service = new AccountAuthenticationService(configProperties, passwordEncoder, accountTokenRepository, restTemplateBuilder);
	}

	@Test
	public void authenticateAccountWhenAccountIsNotFoundShouldThrowException() throws Exception{
		thrown.expect(UsernameNotFoundException.class);
		thrown.expectMessage("Username not found");
		when(restTemplate.exchange(anyString(), eq(HttpMethod.GET),anyObject(), eq(AccountCredential.class)
		)).thenThrow(new RestClientResponseException("User account not found", 404, "not found", null, null, null));
		service.authenticateAccount(new AccountCredential("notfounduser","fakepass"));
	}

	@Test
	public void authenticateAccountWhenPassWordIsIncorrectShouldThrowException() throws Exception{
		thrown.expect(AuthenticationCredentialsNotFoundException.class);
		thrown.expectMessage("Authentication failed");
		ResponseEntity<AccountCredential> responseEntity = new ResponseEntity<>(new AccountCredential("test-user","encrypt-test-pass"), HttpStatus.OK);
		when(restTemplate.exchange(anyString(), eq(HttpMethod.GET),anyObject(), eq(AccountCredential.class)
		)).thenReturn(responseEntity);
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(Boolean.FALSE);
		service.authenticateAccount(new AccountCredential("test-user","fakepass"));
	}

	@Test
	public void getAccountOkWhenUserAndPassIsCorrect() throws Exception{
		ResponseEntity<AccountCredential> responseEntity = new ResponseEntity<>(new AccountCredential("test-user","encrypt-test-pass"), HttpStatus.OK);
		when(restTemplate.exchange(anyString(), eq(HttpMethod.GET),anyObject(), eq(AccountCredential.class)
		)).thenReturn(responseEntity);
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(Boolean.TRUE);
		AuthenticationAccount acc = service.authenticateAccount(new AccountCredential("test-user","test-pass"));
		assertThat( acc.getUsername()).isEqualTo("test-user");
		assertThat( acc.getPassword()).isEqualTo("encrypt-test-pass");
	}

	@Test
	public void generateOTPWhenAccountIsNotFoundShouldThrowException() throws Exception{
		thrown.expect(UsernameNotFoundException.class);
		thrown.expectMessage("Username not found");
		when(restTemplate.exchange(anyString(), eq(HttpMethod.GET),anyObject(), eq(AccountCredential.class)
		)).thenThrow(new RestClientResponseException("User account not found", 404, "not found", null, null, null));
		service.generateOTPProtocol(new AccountCredential("notfounduser","fakepass"));
	}

	@Test
	public void generateOTPWhenPassWordIsIncorrectShouldThrowException() throws Exception{
		thrown.expect(AuthenticationCredentialsNotFoundException.class);
		thrown.expectMessage("Authentication failed");
		ResponseEntity<AccountCredential> responseEntity = new ResponseEntity<>(new AccountCredential("test-user","encrypt-test-pass"), HttpStatus.OK);
		when(restTemplate.exchange(anyString(), eq(HttpMethod.GET),anyObject(), eq(AccountCredential.class)
		)).thenReturn(responseEntity);
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(Boolean.FALSE);
		service.generateOTPProtocol(new AccountCredential("test-user","fakepass"));
	}

	@Test
	public void generateOTPWhenSecretKeyNotProvidedShouldThrowException() throws Exception{
		thrown.expect(MissingTOTPKeyAuthenticatorException.class);
		thrown.expectMessage("No secret key provided");
		ResponseEntity<AccountCredential> responseEntity = new ResponseEntity<>(new AccountCredential("test-user","encrypt-test-pass"), HttpStatus.OK);
		when(restTemplate.exchange(anyString(), eq(HttpMethod.GET),anyObject(), eq(AccountCredential.class)
		)).thenReturn(responseEntity);
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(Boolean.TRUE);
		service.generateOTPProtocol(new AccountCredential("test-user","fakepass"));
	}

	@Test
	public void generateOTPOkWhenAllDataIsCorrect() throws Exception{
		ResponseEntity<AccountCredential> responseEntity = new ResponseEntity<>(new AccountCredential("test-user","encrypt-test-pass","test-secret-key"), HttpStatus.OK);
		when(restTemplate.exchange(anyString(), eq(HttpMethod.GET),anyObject(), eq(AccountCredential.class)
		)).thenReturn(responseEntity);
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(Boolean.TRUE);
		String optStringProtocol = service.generateOTPProtocol(new AccountCredential("test-user","fakepass"));

		assertThat(optStringProtocol).isEqualTo(String.format("otpauth://totp/%s?secret=%s&issuer=SpringBootTOTP", responseEntity.getBody().getUserName(), responseEntity.getBody().getSecret()));
	}

}