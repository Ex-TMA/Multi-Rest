package gateway;

/**
 * Created by nsonanh on 17/07/2017.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(ApiGatewayServiceConfiguration.class,args);
    }

}