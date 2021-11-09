package de.blutmondgilde.unity.config;

import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import de.blutmondgilde.unity.oauth.DiscordOAuth2AccessTokenResponseClient;
import de.blutmondgilde.unity.oauth.DiscordOAuth2Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SecurityConfig extends VaadinWebSecurityConfigurerAdapter {
    public static final String DISCORD_BOT_USER_AGENT = "DiscordBot (https://unity.blutmondgilde.de)";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.oauth2Login()
            .tokenEndpoint().accessTokenResponseClient(new DiscordOAuth2AccessTokenResponseClient(restOperations()))
            .and()
            .userInfoEndpoint().userService(new DiscordOAuth2Service(restOperations()));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
            "/images/**"
        );

        super.configure(web);
    }

    @Bean
    public RestOperations restOperations() {
        return new RestTemplate();
    }
}
