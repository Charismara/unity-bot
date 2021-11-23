package de.blutmondgilde.unity.view;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

/**
 * Adds an explicit link that the user has to click to login.
 */
@Route(value = "login", layout = DefaultLayout.class)
@PageTitle("Login")
public class LoginScreen extends VerticalLayout {
    private static final String URL_DISCORD = "/oauth2/authorization/discord";

    @Value("${spring.security.oauth2.client.registration.discord.client-id}")
    private String discordKey;

    public LoginScreen() {
        setPadding(true);
        setAlignItems(Alignment.CENTER);
    }

    @PostConstruct
    public void initView() {
        // Check that oauth keys are present
        if (checkDiscordKey()) {
            Paragraph text = new Paragraph("Could not find OAuth client key in application.properties.");
            text.getStyle().set("padding-top", "100px");
            add(text);
        } else {

            Anchor discordButton = new Anchor(URL_DISCORD, "Login with Discord");
            discordButton.getStyle().set("margin-top", "100px");
            discordButton.getElement().setAttribute("router-ignore", true);
            add(discordButton);
        }

    }

    private boolean checkDiscordKey() {
        return discordKey == null || discordKey.isEmpty() || discordKey.length() < 16;
    }
}
