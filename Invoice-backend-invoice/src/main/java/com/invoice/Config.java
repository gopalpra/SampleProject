package com.invoice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invoice.constant.InterceptorAPIs;
import com.invoice.interceptor.AccessTokenInterceptor;
import com.invoice.interceptor.AdminScopeInterceptor;
import com.invoice.util.HTTPResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

@Configuration
public class Config implements WebMvcConfigurer
{
    public static Logger logger = LoggerFactory.getLogger(Config.class);
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Autowired
    private AccessTokenInterceptor accessTokenInterceptor;

    @Autowired
    private AdminScopeInterceptor adminScopeInterceptor;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    @PostConstruct
    public void Column()
    {
        logger.info("url: {}"+url);
        logger.info("username: {}"+username);
        logger.info("password: {}"+password);
        System.out.println("url: {}"+url);
        System.out.println("username: {}"+username);
        System.out.println("password: {}"+password);
    }





    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(accessTokenInterceptor).addPathPatterns(InterceptorAPIs.apiWithAccessTokenInterceptor);
        registry.addInterceptor(adminScopeInterceptor).addPathPatterns(InterceptorAPIs.apiWithAdminScopeInterceptor);
    }
    @Bean
    public RestTemplate restTemplate() {

        return new RestTemplate();
    }

    @Bean
    public HTTPResponse httpResponse()
    {
        return new HTTPResponse();
    }

}