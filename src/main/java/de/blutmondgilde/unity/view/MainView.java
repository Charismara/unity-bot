
package de.blutmondgilde.unity.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.blutmondgilde.unity.SecurityService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

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

        OAuth2AuthenticationToken token = securityService.getOAuth2AuthenticationToken();
        WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();

        Div authentication = new Div();
        authentication.setText("");

        Button logout = new Button("Logout");
        logout.addClickListener(buttonClickEvent -> securityService.logout());

        setAlignItems(Alignment.CENTER);
        add(div, data, authentication, logout);
    }
}
