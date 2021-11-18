
package com.vaadin.example.oauth.ui;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;

/**
 * Application main class that is hidden to user before authentication.
 */
@PageTitle("Home")
@Route
public class MainView extends VerticalLayout {
    @PostConstruct
    public void init() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Div div = new Div();
        div.setText("Hello " + authentication.getName());
        div.getElement().getStyle().set("font-size", "xx-large");

        // Spring maps the 'logout' url so we should ignore it
        Anchor logout = new Anchor("/logout", "Logout");
        logout.getElement().setAttribute("router-ignore", true);

        setAlignItems(Alignment.CENTER);
        add(div, logout);
    }
}
