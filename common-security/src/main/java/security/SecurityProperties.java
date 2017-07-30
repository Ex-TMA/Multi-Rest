package security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by nsonanh on 30/07/2017.
 */
@ConfigurationProperties(value = "classpath:application.properties", ignoreUnknownFields = false, prefix = "security")
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
