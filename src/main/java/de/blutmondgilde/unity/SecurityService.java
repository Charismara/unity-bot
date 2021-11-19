package de.blutmondgilde.unity;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import de.blutmondgilde.unity.data.DiscordOAuthUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {
    public DiscordOAuthUser getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof DiscordOAuthUser) {
            return (DiscordOAuthUser) principal;
        }
        // Anonymous or no authentication.
        return null;
    }

    public OAuth2AuthenticationToken getOAuth2AuthenticationToken() {
        return (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

    public OAuth2AccessToken getAccessToken(OAuth2AuthorizedClientService clientService) {
        OAuth2AuthenticationToken token = getOAuth2AuthenticationToken();
        return clientService.loadAuthorizedClient(token.getAuthorizedClientRegistrationId(), token.getName()).getAccessToken();
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation("/");
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
    }
}
