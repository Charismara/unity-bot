package de.blutmondgilde.unity;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
@Theme(value = "unity", variant = Lumo.DARK)
@NpmPackage(value = "line-awesome", version = "1.3.0")
@Push
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

    /**
     * Runs the Spring Boot application.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
