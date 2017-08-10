package config.property;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

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

}
