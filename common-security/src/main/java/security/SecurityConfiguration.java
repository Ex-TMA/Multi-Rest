package security;

import config.property.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import security.filter.StatelessAuthenticationFilter;
import security.model.AuthenticationAccount;

import java.security.SecureRandom;

/**
 * Created by nsonanh on 02/08/2017.
 */
@Configuration
@ComponentScan
@EnableWebSecurity
@EnableConfigurationProperties({SecurityProperties.class, ConfigProperties.class})
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ConfigProperties configProperties;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    AccountAuthenticationService accountAuthenticationService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceBean());
    }

    @Override
    public UserDetailsService userDetailsServiceBean() {
        return userName -> {
            AuthenticationAccount account = accountAuthenticationService.getAuthenticationAccountFromAccountService(userName);
            return new org.springframework.security.core.userdetails.User(userName, account.getPassword(), true, true, true, true,
                    AuthorityUtils.createAuthorityList("USER"));
        };
    }

    @Bean
    public AccountAuthenticationService accountAuthService() {
        return new AccountAuthenticationService();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        tokenAuthenticationService.setTokenService(tokenService());
        http
                .csrf()
                .disable()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/api/login").permitAll()
                .antMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .addFilterAfter(new StatelessAuthenticationFilter(tokenAuthenticationService), BasicAuthenticationFilter.class);
    }

    @Bean
    public TokenService tokenService() {
        KeyBasedPersistenceTokenService res = new KeyBasedPersistenceTokenService();
        res.setSecureRandom(new SecureRandom());
        res.setServerSecret(securityProperties.getServerSecret());
        res.setServerInteger(securityProperties.getServerInteger());

        return res;
    }

    @Bean
    public PasswordEncoder encoder() throws Exception {
        Class<?> encoderClass = Class.forName(configProperties.getEncoderClass());
        return (PasswordEncoder) encoderClass.newInstance();
    }
}
