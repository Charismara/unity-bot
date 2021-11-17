package de.blutmondgilde.unity.config;

import com.vaadin.flow.spring.security.VaadinSavedRequestAwareAuthenticationSuccessHandler;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import de.blutmondgilde.unity.security.SessionRepository;
import de.blutmondgilde.unity.security.SessionRepositoryListener;
import de.blutmondgilde.unity.security.VaadinAwareSecurityContextHolderStrategy;
import de.blutmondgilde.unity.service.CustomOAuth2UserService;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends VaadinWebSecurityConfigurerAdapter {
    final ClientRegistrationRepository clientRegistrationRepository;
    final GrantedAuthoritiesMapper authoritiesMapper;
    final CustomOAuth2UserService oAuth2UserService;

    SecurityConfig(ClientRegistrationRepository clientRegistrationRepository, GrantedAuthoritiesMapper authoritiesMapper, CustomOAuth2UserService oAuth2UserService) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authoritiesMapper = authoritiesMapper;
        this.oAuth2UserService = oAuth2UserService;
        SecurityContextHolder.setStrategyName(VaadinAwareSecurityContextHolderStrategy.class.getName());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/login/**","/oauth/**").permitAll();
        super.configure(http);
        http.oauth2Login(oauthLogin -> {
                oauthLogin.clientRegistrationRepository(clientRegistrationRepository)
                    .userInfoEndpoint(userInfoEndpoint -> {
                        userInfoEndpoint.userAuthoritiesMapper(authoritiesMapper);
                        userInfoEndpoint.userService(oAuth2UserService);
                    })
                    .successHandler(new VaadinSavedRequestAwareAuthenticationSuccessHandler());
            })
            .logout(logout -> {
                logout.logoutSuccessHandler(logoutSuccessHandler())
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"));
            });
    }

    private OidcClientInitiatedLogoutSuccessHandler logoutSuccessHandler() {
        var logoutSuccessHandler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        logoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/");
        return logoutSuccessHandler;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().antMatchers(
            "/images/**"
        );
        web.ignoring().antMatchers("/logged-out", "/session-expired", "/back-channel-logout");
    }

    @Bean
    public SessionRepository sessionRepository() {
        return new SessionRepository();
    }

    @Bean
    public ServletListenerRegistrationBean<SessionRepositoryListener> sessionRepositoryListener() {
        var bean = new ServletListenerRegistrationBean<SessionRepositoryListener>();
        bean.setListener(new SessionRepositoryListener(sessionRepository()));
        return bean;
    }
}
