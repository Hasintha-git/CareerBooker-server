package com.careerbooker.server.config;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://localhost:3306/careerbooker")
                .username("root")
                .password("")
                .build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurer()
        {
            @Override
            public void addCorsMappings(CorsRegistry registry)
            {
                registry.addMapping("/**").allowedMethods("*").allowedOrigins("*");
            }
        };
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("messages"); // Name of your properties file without the extension
        source.setDefaultEncoding("UTF-8");
        return source;
    }
}
