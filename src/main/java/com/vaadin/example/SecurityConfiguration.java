package com.vaadin.example;

import com.vaadin.example.oauth.ui.LoginScreen;
import com.vaadin.flow.server.HandlerHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurityConfigurerAdapter {

    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_URL = "/logout";
    private static final String LOGOUT_SUCCESS_URL = "/";

    /**
     * Registers our UserDetailsService and the password encoder to be used on login
     * attempts.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // Allow all flow internal requests.
            .authorizeRequests().requestMatchers(SecurityConfiguration::isFrameworkInternalRequest).permitAll()
            // Not using Spring CSRF here to be able to use plain HTML for the login page
            .and().csrf().disable()
            // Configure logout
            .logout().logoutUrl(LOGOUT_URL).logoutSuccessUrl(LOGOUT_SUCCESS_URL)
            // Configure the login page with OAuth.
            .and().oauth2Login().loginPage(LOGIN_URL).permitAll()
            .tokenEndpoint().accessTokenResponseClient(accessTokenResponseClient())
            .and()
            .userInfoEndpoint().userService(userService());

        //Configure Vaadin
        super.configure(http);
        //Set LoginScreen as login view
        setLoginView(http, LoginScreen.class,LOGOUT_SUCCESS_URL);
    }

    /**
     * Allows access to static resources, bypassing Spring Security.
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
            // client-side JS code
            "/VAADIN/**",

            // client-side JS code
            "/frontend/**",

            // the standard favicon URI
            "/favicon.ico",

            // web application manifest
            "/manifest.webmanifest", "/sw.js", "/offline-page.html",

            // icons and images
            "/icons/**", "/images/**");

        try {
            super.configure(web);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests if the request is an internal framework request. The test consists of
     * checking if the request parameter is present and if its value is consistent
     * with any of the request types know.
     *
     * @param request {@link HttpServletRequest}
     *
     * @return true if is an internal framework request. False otherwise.
     */
    static boolean isFrameworkInternalRequest(HttpServletRequest request) {
        final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
        return parameterValue != null && Stream.of(HandlerHelper.RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
    }

    static RequestEntity<?> withUserAgent(RequestEntity<?> request) {
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(request.getHeaders());
        headers.add(HttpHeaders.USER_AGENT, DISCORD_BOT_USER_AGENT);

        return new RequestEntity<>(request.getBody(), headers, request.getMethod(), request.getUrl());
    }

    private static final String DISCORD_BOT_USER_AGENT = "DiscordBot (https://github.com/fourscouts/blog/tree/master/oauth2-discord)";

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient client = new DefaultAuthorizationCodeTokenResponseClient();

        client.setRequestEntityConverter(new OAuth2AuthorizationCodeGrantRequestEntityConverter() {
            @Override
            public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest oauth2Request) {
                return withUserAgent(super.convert(oauth2Request));
            }
        });

        return client;
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> userService() {
        DefaultOAuth2UserService service = new DefaultOAuth2UserService();

        service.setRequestEntityConverter(new OAuth2UserRequestEntityConverter() {
            @Override
            public RequestEntity<?> convert(OAuth2UserRequest userRequest) {
                return withUserAgent(super.convert(userRequest));
            }
        });

        return service;
    }
}
