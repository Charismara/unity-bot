package de.blutmondgilde.unity.service;

import de.blutmondgilde.unity.data.DiscordOAuthUser;
import de.blutmondgilde.unity.data.User;
import de.blutmondgilde.unity.data.UserRepository;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import static de.blutmondgilde.unity.SecurityConfiguration.withUserAgent;

@Service
public class UnityOAuth2UserService extends DefaultOAuth2UserService {
    private UserRepository repo;

    public UnityOAuth2UserService(UserRepository repo) {
        this.repo = repo;
        setRequestEntityConverter(new OAuth2UserRequestEntityConverter() {
            @Override
            public RequestEntity<?> convert(OAuth2UserRequest userRequest) {
                return withUserAgent(super.convert(userRequest));
            }
        });
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        return new DiscordOAuthUser(user);
    }

    public void processOAuthPostLogin(OAuth2User oAuth2User) {
        DiscordOAuthUser discordOAuthUser = new DiscordOAuthUser(oAuth2User);
        if (discordOAuthUser.getDiscordId() != -1) {
            User user = repo.findById(discordOAuthUser.getDiscordId()).orElseGet(() -> User.from(discordOAuthUser));
            repo.save(user);
        }
    }
}
