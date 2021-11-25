package de.blutmondgilde.unity.service;

import de.blutmondgilde.unity.data.discordapi.Guild;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

//Helper class to make API calls to the Discord API
public class DiscordAPIHelper {
    private final OAuth2AuthorizedClient currentClient;
    private final WebClient webClient;

    public DiscordAPIHelper(SecurityService securityService) {
        this.currentClient = securityService.getCurrentOAuthClient();
        HttpClient httpClient = HttpClient.create();
        httpClient.responseTimeout(Duration.ofSeconds(15));
        this.webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .baseUrl("https://discord.com/api")
            .defaultHeaders(httpHeaders -> httpHeaders.setBearerAuth(currentClient.getAccessToken().getTokenValue()))
            .build();
    }

    public Mono<Guild[]> getGuilds() {
        return this.webClient.get().uri("/users/@me/guilds")
            .retrieve()
            .bodyToMono(Guild[].class);
    }
}
