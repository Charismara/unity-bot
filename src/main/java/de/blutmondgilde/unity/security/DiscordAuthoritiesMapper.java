package de.blutmondgilde.unity.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
public class DiscordAuthoritiesMapper implements GrantedAuthoritiesMapper {

    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
            .filter(OAuth2UserAuthority.class::isInstance)
            .map(OAuth2UserAuthority.class::cast)
            .findFirst()
            .map(this::extractClientRoles)
            .orElse(Collections.emptyList());
    }

    private Collection<? extends GrantedAuthority> extractClientRoles(OAuth2UserAuthority oauthAuthority) {
        /*
        var resourceAccess = (Map<String, Object>) oauthAuthority.getAttributes().getOrDefault("resource_access", Collections.emptyMap());
        var clientAccess = (Map<String, Object>) resourceAccess.getOrDefault(clientId, Collections.emptyMap());
        var roles = (Collection<String>) clientAccess.get("roles");

        /return roles.stream().map(r -> "ROLE_" + r.toUpperCase()).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
         */
        return Collections.singletonList(new OAuth2UserAuthority(oauthAuthority.getAttributes()));
    }
}
