package config.filter;

import config.property.ConfigProperties;
import config.RequestHeader;
import config.property.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.GenericFilterBean;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by truongnguyen on 8/8/17.
 */
public class CheckPassKeyFilter extends GenericFilterBean {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ConfigProperties configProperties;
    @Autowired
    private Utils utils;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        logger.info("Checking passkey");
        HttpServletRequest request = (HttpServletRequest)req;
        String passkey = request.getHeader(RequestHeader.GATEWAY_PASSKEY);
        /*if(utils.match(configProperties.getGatewayPasskey(), passkey)){
            chain.doFilter(req, res);
        }
        else{
            throw new AuthenticationException();
        }*/
        chain.doFilter(req, res);

    }
}
