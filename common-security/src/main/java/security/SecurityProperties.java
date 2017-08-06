package security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by nsonanh on 30/07/2017.
 */
@PropertySource(("security.properties"))
@ConfigurationProperties(prefix = "security")
@Component
public class SecurityProperties {
    private String encryptionMethodClass;
    private String serverSecret;
    private Integer serverInteger;

    public String getEncryptionMethodClass() {
        return encryptionMethodClass;
    }

    public void setEncryptionMethodClass(String encryptionMethodClass) {
        this.encryptionMethodClass = encryptionMethodClass;
    }

    public String getServerSecret() {
        return serverSecret;
    }

    public void setServerSecret(String serverSecret) {
        this.serverSecret = serverSecret;
    }

    public Integer getServerInteger() {
        return serverInteger;
    }

    public void setServerInteger(Integer serverInteger) {
        this.serverInteger = serverInteger;
    }
}
