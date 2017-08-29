package security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nsonanh on 29/07/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationAccount implements UserDetails {

    private String username;

    private String pass;

    private String secret;

    public AuthenticationAccount() {
    }

    public AuthenticationAccount(String username) {
        this.username = username;
    }

    public AuthenticationAccount(String username, String pass) {
        this.username = username;
        this.pass = pass;
    }

    public AuthenticationAccount(String username, String pass, String secret) {
        this.username = username;
        this.pass = pass;
        this.secret = secret;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getSecret() {
        return secret;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("USER");
        Set<GrantedAuthority> res = new HashSet<GrantedAuthority>();
        res.add(authority);
        return res;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return pass;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}