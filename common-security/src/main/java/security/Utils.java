package security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.Token;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

/**
 * Created by nsonanh on 27/07/2017.
 */
public class Utils {
    static Log log = LogFactory.getLog(Utils.class.getName());

    protected static final long DAY = 1000 * 60 * 60 * 24;

    private static PasswordEncoder encoder;
    private static KeyBasedPersistenceTokenService tokenService;

    static {
        SecurityProperties securityProperties = new SecurityProperties();
        try {
            // Encoder
            Class<?> encoderClass = Class.forName(securityProperties.getEncryptionMethodClass());
            encoder = (PasswordEncoder) encoderClass.newInstance();

            // Token Service
            tokenService = new KeyBasedPersistenceTokenService();
            tokenService.setSecureRandom(new SecureRandom());
            tokenService.setServerSecret(securityProperties.getServerSecret());
            tokenService.setServerInteger(securityProperties.getServerInteger());
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

    // Token
    public static Token generateToken(String userName)
    {
        return tokenService.allocateToken(userName);
    }
    public static Token verifyToken(String tokenString)
    {
        return tokenService.verifyToken(tokenString);
    }
}
