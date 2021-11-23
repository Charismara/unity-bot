
package de.blutmondgilde.unity.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.blutmondgilde.unity.SecurityService;

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
        Div div = new Div();
        div.setText("Hallo " + securityService.getAuthenticatedUser().getName());
        div.addClassName("font-size-xxl");

        Div data = new Div();
        data.setText("Attribute: " + securityService.getAuthenticatedUser().getAttributes().toString());

        Div authentication = new Div();
        Paragraph p1 = new Paragraph("Loaded Service Client: " + securityService.getAccessToken().getTokenValue());
        authentication.add(p1);

        Button logout = new Button("Logout");
        logout.addClickListener(buttonClickEvent -> securityService.logout());

        setAlignItems(Alignment.CENTER);
        add(div, data, authentication, logout);
    }
}
