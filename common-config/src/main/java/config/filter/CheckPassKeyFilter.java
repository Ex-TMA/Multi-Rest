package config.filter;

import config.RequestHeader;
import config.property.ConfigProperties;
import config.property.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by truongnguyen on 8/8/17.
 */
public class CheckPassKeyFilter extends GenericFilterBean {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ConfigProperties configProperties;
    private final Utils utils;

    public CheckPassKeyFilter(ConfigProperties configProperties, Utils utils) {
        this.configProperties = configProperties;
        this.utils = utils;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        logger.info("Checking passkey");
        HttpServletRequest request = (HttpServletRequest)req;
        String passkey = request.getHeader(RequestHeader.GATEWAY_PASSKEY);
        logger.info("Gateway passkey " + passkey);
        logger.info("Config passkey " + configProperties.getGatewayPasskey());
        if(utils.match(configProperties.getGatewayPasskey(), passkey)){
            chain.doFilter(req, res);
        }
        else{
            HttpServletResponse response = ((HttpServletResponse) res);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\": \"Not a request from gateway\"}");
            response.getWriter().close();
        }
    }
}
