package config;

import config.filter.CheckPassKeyFilter;
import config.property.ConfigProperties;
import config.property.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
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
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class })
public class CommonConfig {

    Logger logger = LoggerFactory.getLogger(CommonConfig.class);

    @Bean
    public FilterRegistrationBean auditFilterRegistration(ConfigProperties configProperties, Utils utils) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new CheckPassKeyFilter(configProperties, utils));
        filterRegistrationBean.setOrder(3); // ordering in the filter chain
        return filterRegistrationBean;
    }

    @Bean
    public PasswordEncoder encoder(ConfigProperties configProperties) throws Exception{
        Class<?> encoderClass = Class.forName(configProperties.getEncoderClass());
        return (PasswordEncoder) encoderClass.newInstance();
    }

}
