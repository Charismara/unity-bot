package de.blutmondgilde.unity;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {
    public DefaultOAuth2User getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();

        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof DefaultOAuth2User) {
            return (DefaultOAuth2User) principal;
        }
        // Anonymous or no authentication.
        return null;
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation("/");
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
    }
}
