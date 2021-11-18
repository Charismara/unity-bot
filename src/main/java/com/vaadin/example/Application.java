package com.vaadin.example;

import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

    /**
     * Runs the Spring Boot application.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
