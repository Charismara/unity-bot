
package com.vaadin.example.oauth.ui;

import com.vaadin.example.SecurityService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

/**
 * Application main class that is hidden to user before authentication.
 */
@PageTitle("Home")
@Route
@PermitAll
public class MainView extends VerticalLayout {
    SecurityService securityService;

    MainView(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostConstruct
    public void init() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Div div = new Div();
        div.setText("Hello " + authentication.getName());
        div.getElement().getStyle().set("font-size", "xx-large");

        Button logout = new Button("Logout");
        logout.addClickListener(buttonClickEvent -> securityService.logout());

        setAlignItems(Alignment.CENTER);
        add(div, logout);
    }
}
