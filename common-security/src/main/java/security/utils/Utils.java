package security.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by nsonanh on 27/07/2017.
 */
public class Utils {
    static Log log = LogFactory.getLog(Utils.class.getName());

    private static PasswordEncoder encoder;
    private static final String PROP_CLASS_PATH = "application.properties";
    private static final String ENCRYPTION_METHOD_PROPERTY = "security.encryption.method.class";

    static {
        Resource resource = new ClassPathResource(PROP_CLASS_PATH);
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            String encryptClassName = properties.getProperty(ENCRYPTION_METHOD_PROPERTY);
            Class<?> encoderClass = Class.forName(encryptClassName);

            encoder = (PasswordEncoder) encoderClass.newInstance();
        } catch (IOException e) {
            log.error("IOException in Utils", e);
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException in Utils", e);
        } catch (InstantiationException e) {
            log.error("InstantiationException in Utils", e);
        } catch (ClassNotFoundException e) {
            log.error("ClassNotFoundException in Utils", e);
        }
    }

    public static String encode(String rawPass){
        return encoder.encode(rawPass);
    }
    public static boolean match(String rawPass, String encodedPass){
        return encoder.matches(rawPass, encodedPass);
    }
}
