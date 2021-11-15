package de.blutmondgilde.unity.views.home;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import de.blutmondgilde.unity.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Home")
@Route("")
@AnonymousAllowed
public class HomeView extends VerticalLayout {
    private final SecurityService securityService;

    public HomeView(@Autowired SecurityService securityService) {
        this.securityService = securityService;
        setup();
    }

    private void setup() {
        setSpacing(false);
        setPadding(false);
        add(navbar());
        add(body());
    }

    private Component navbar() {
        HorizontalLayout root = new HorizontalLayout();
        root.setWidthFull();
        root.setAlignItems(Alignment.CENTER);
        Span name = new Span("Unity");
        name.getStyle().set("padding-left", "1rem");
        root.add(name);

        if (this.securityService.getAuthenticatedUser() != null) {
            Notification.show("Logged In. User: " + securityService.getAuthenticatedUser().getUsername());
            Button logoutButton = new Button("Logout", click -> securityService.logout());
            root.add(logoutButton);
        } else {
            Button loginButton = new Button();
            Image discordLogo = new Image("images/discord.svg", "discord_logo.png");
            discordLogo.getStyle().set("padding-top", "0.5rem");
            loginButton.setIcon(discordLogo);
            loginButton.setText("Login with Discord");
            loginButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            loginButton.getStyle().set("padding-right", "1rem");
            loginButton.addClassName("toolbar");
            Anchor anchor = new Anchor("/oauth2/authorization/discord", loginButton);
            anchor.getElement().setAttribute("router-ignore", true);
            root.add(anchor);
        }

        root.setFlexGrow(1, name);
        root.addClassNames("contrast-5pct");

        return root;
    }


    private Component body() {
        VerticalLayout root = new VerticalLayout();
        Image img = new Image("images/empty-plant.png", "placeholder plant");
        img.setWidth("200px");
        root.add(img);

        root.add(new H2("This place intentionally left empty"));
        root.add(new Paragraph("It’s a place where you can grow your own UI 🤗"));

        root.setSizeFull();
        root.setJustifyContentMode(JustifyContentMode.CENTER);
        root.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        root.getStyle().set("text-align", "center");

        return root;
    }
}
