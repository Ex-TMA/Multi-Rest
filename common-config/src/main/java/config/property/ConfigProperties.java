package config.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by truongnguyen on 8/9/17.
 */
@Component
@ConfigurationProperties(prefix = "common.config")
@PropertySource({"classpath:app-config.properties","classpath:app-config-override.properties" })
public class ConfigProperties {

    private String encoderClass;

    private String gatewayPasskey;

    private String checkPasskeyEnabled;

    public ConfigProperties() {
    }

    public String getEncoderClass() {
        return encoderClass;
    }

    public void setEncoderClass(String encoderClass) {
        this.encoderClass = encoderClass;
    }

    public String getGatewayPasskey() {
        return gatewayPasskey;
    }

    public void setGatewayPasskey(String gatewayPasskey) {
        this.gatewayPasskey = gatewayPasskey;
    }

    public String getCheckPasskeyEnabled() {
        return checkPasskeyEnabled;
    }

    public void setCheckPasskeyEnabled(String checkPasskeyEnabled) {
        this.checkPasskeyEnabled = checkPasskeyEnabled;
    }
}
