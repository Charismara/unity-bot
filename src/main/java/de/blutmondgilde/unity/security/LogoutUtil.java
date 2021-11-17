package de.blutmondgilde.unity.security;

import com.vaadin.flow.component.UI;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class LogoutUtil {
    private final String relativeLogoutUrl;

    LogoutUtil(ServerProperties serverProperties) {
        relativeLogoutUrl = UriComponentsBuilder.fromPath(serverProperties.getServlet().getContextPath()).path("logout").build().toUriString();
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation(relativeLogoutUrl);
    }
}
