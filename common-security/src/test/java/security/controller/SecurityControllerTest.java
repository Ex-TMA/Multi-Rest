package security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.token.DefaultToken;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.token.TokenService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import security.SecurityTestBootConfiguration;
import security.model.AccountCredential;
import security.model.AuthenticationAccount;
import security.service.AccountAuthenticationService;
import security.service.TOTPAuthenticationService;

import java.util.Date;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers=SecurityController.class, secure = false)
@ContextConfiguration(classes={SecurityTestBootConfiguration.class})
@TestPropertySource(properties = {"common.config.checkPasskeyEnabled=false"})
public class SecurityControllerTest {
	@MockBean
	private TokenService tokenService;

	@MockBean
	private AccountAuthenticationService customerAuthService;

	@MockBean
	private TOTPAuthenticationService totpAuthenticationService;

	@Autowired
	private MockMvc mvc;

	@Test
	public void doAuth()throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		AuthenticationAccount extendedInformation = new AuthenticationAccount("test-user", "test-pass");
		Token testToken = new DefaultToken("test-01234566", new Date().getTime(),mapper.writeValueAsString(extendedInformation));

		when(customerAuthService.authenticateAccount(anyObject())).thenReturn(extendedInformation);
		when(tokenService.allocateToken(anyString())).thenReturn(testToken);
		AccountCredential accountCredential = new AccountCredential("test-user","test-pass");
		mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(accountCredential)))
				.andExpect(status().isOk());
		verify(totpAuthenticationService).authenticateTOTPUser(anyObject(), eq(extendedInformation));
		verify(customerAuthService).saveAccountToken(anyString(), anyString(), anyString());
		verify(tokenService).verifyToken(anyString());
	}

}