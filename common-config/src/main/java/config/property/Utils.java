package config.property;


import org.apache.commons.codec.binary.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * Created by truongnguyen on 7/23/17.
 */
@Component
public class Utils {

    @Autowired
    private PasswordEncoder encoder;

    public String encode(String rawPass){
        return encoder.encode(rawPass);
    }
    public boolean match(String rawPass, String encodedPass){
        return encoder.matches(rawPass, encodedPass);
    }

    public String generateSecret() {
        byte [] buffer = new byte[10];
        new SecureRandom().nextBytes(buffer);
        return new String(new Base32().encode(buffer));
    }
}
