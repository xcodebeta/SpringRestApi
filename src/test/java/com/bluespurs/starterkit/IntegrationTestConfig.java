package com.bluespurs.starterkit;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

public class IntegrationTestConfig extends ApplicationConfig {
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:test;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false");

        return dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Use a low strength for tests.
        return new BCryptPasswordEncoder(6);
    }
}
