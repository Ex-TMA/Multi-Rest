package security.model;

import config.model.BaseEntity;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by truongnguyen on 7/18/17.
 */
@Entity
public class AccountToken extends BaseEntity {

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @NotBlank
    private String token;
    @NotBlank
    private String ip;
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresAt;
    @NotBlank
    private String userName;

    protected AccountToken() {
    }

    public AccountToken(String userName, String token, String ip, Date expiresAt) {
        this.token = token;
        this.ip = ip;
        this.expiresAt = expiresAt;
        this.userName = userName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString(){
        return String.format("AccountToken[id=%d, token=%s]",id, token);
    }
}