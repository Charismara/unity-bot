package de.blutmondgilde.unity.service;

import com.vaadin.flow.spring.security.VaadinSavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UnityAuthenticationSuccessHandler extends VaadinSavedRequestAwareAuthenticationSuccessHandler {
    UnityOAuth2UserService userService;

    public UnityAuthenticationSuccessHandler(UnityOAuth2UserService userService){
        super();
        this.userService=userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User user = (OAuth2User) authentication.getPrincipal();
            userService.processOAuthPostLogin(user);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
