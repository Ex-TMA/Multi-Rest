package security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by nsonanh on 27/07/2017.
 */
public class Utils {

    @Autowired
    private static SecurityProperties securityProperties;

    static Log log = LogFactory.getLog(Utils.class.getName());

    private static PasswordEncoder encoder;

    static {
        try {
            // Encoder
            Class<?> encoderClass = Class.forName(securityProperties.getEncryptionMethodClass());
            encoder = (PasswordEncoder) encoderClass.newInstance();

        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException in Utils", e);
        } catch (InstantiationException e) {
            log.error("InstantiationException in Utils", e);
        } catch (ClassNotFoundException e) {
            log.error("ClassNotFoundException in Utils", e);
        }
    }

    // Auth
    public static String encode(String rawPass){
        return encoder.encode(rawPass);
    }
    public static boolean match(String rawPass, String encodedPass){
        return encoder.matches(rawPass, encodedPass);
    }
}
