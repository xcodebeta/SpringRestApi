package com.bluespurs.starterkit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableCaching
public class ApplicationConfig extends SpringBootServletInitializer {
    @Value("${http.enable-cors}")
    private boolean enableCors;
    @Value("${base-package}")
    private String basePackage;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfig.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
        return applicationBuilder.sources(ApplicationConfig.class);
    }

    @Bean
    public WebMvcConfigurerAdapter webConfig() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                super.addCorsMappings(registry);

                if (enableCors) {
                    registry.addMapping("/**")
                            .allowedMethods("GET", "POST", "PUT", "DELETE");
                }
            }
        };
    }
}
