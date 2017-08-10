package config;

import config.filter.CheckPassKeyFilter;
import config.property.ConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by truongnguyen on 8/9/17.
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class CommonConfig {

    Logger logger = LoggerFactory.getLogger(CommonConfig.class);

    @Bean
    public FilterRegistrationBean auditFilterRegistration() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new CheckPassKeyFilter());
        filterRegistrationBean.setOrder(3); // ordering in the filter chain
        return filterRegistrationBean;
    }

    @Bean
    public PasswordEncoder encoder(ConfigProperties configProperties) throws Exception{
        logger.debug(configProperties == null?"null roi": "hahaha");
        Class<?> encoderClass = Class.forName(configProperties.getEncoderClass());
        return (PasswordEncoder) encoderClass.newInstance();
    }

}
