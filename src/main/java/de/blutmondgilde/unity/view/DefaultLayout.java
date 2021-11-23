package de.blutmondgilde.unity.view;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;
import de.blutmondgilde.unity.SecurityService;


public class DefaultLayout extends VerticalLayout implements RouterLayout {
    private static final String URL_DISCORD = "/oauth2/authorization/discord";
    SecurityService securityService;

    public DefaultLayout(SecurityService securityService) {
        this.securityService = securityService;
        setMargin(false);
        setPadding(false);
        add(createNavbar());
    }

    private HorizontalLayout createNavbar() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.addClassName("contrast-10pct");
        layout.addClassName("no-margin-left");
        layout.addClassName("no-margin-right");

        Span name = new Span("Unity");
        name.addClassName("font-size-xxl");
        name.getStyle().set("padding-left", "1rem");
        layout.add(name);

        if (!securityService.isLoggedIn()) {
            layout.add(createLoginButton());
        }

        layout.setFlexGrow(1, name);
        return layout;
    }

    private Anchor createLoginButton() {
        Anchor login = new Anchor(URL_DISCORD);
        login.getElement().setAttribute("router-ignore", true);

        HorizontalLayout anchorLayout = new HorizontalLayout();
        anchorLayout.setSpacing(false);
        anchorLayout.getStyle().set("padding-top", "0.25rem");
        anchorLayout.getStyle().set("padding-right", "1rem");

        anchorLayout.setAlignItems(Alignment.CENTER);
        anchorLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        Image discordLogo = new Image("img/Discord-emblem.png", "Image not found");
        discordLogo.setWidth(4.0F, Unit.REM);
        anchorLayout.add(discordLogo);

        Span loginText = new Span("Login with Discord");
        loginText.addClassName("font-size-xl");
        anchorLayout.add(loginText);

        login.add(anchorLayout);
        return login;
    }
}
