package de.blutmondgilde.unity.data;

import com.vaadin.flow.component.html.Image;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@AllArgsConstructor
public class DiscordOAuthUser implements OAuth2User {
    private final OAuth2User oAuth2User;

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    private Set<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet(Comparator.comparing(GrantedAuthority::getAuthority));
        sortedAuthorities.addAll(authorities);
        return sortedAuthorities;
    }

    @Override
    public String getName() {
        return oAuth2User.getName();
    }

    public String getAvatarHash() {
        return oAuth2User.getAttribute("avatar");
    }

    public Image getAvatarImage(AvatarType avatarType) {
        return new Image("https://cdn.discordapp.com/avatars/" + getDiscordId() + "/" + getAvatarHash() + "." + avatarType.getType(), "Discord-Icon.png");
    }

    public String getEMail() {
        return oAuth2User.getAttribute("email");
    }

    public long getDiscordId() {
        String id = oAuth2User.getAttribute("id");
        return Long.parseLong(id == null ? "-1" : id);
    }

    public String getDiscriminator() {
        return oAuth2User.getAttribute("discriminator");
    }

    @Nullable
    public String getBannerHash() {
        return oAuth2User.getAttribute("banner");
    }

    @Nullable
    public String getBannerColor() {
        return oAuth2User.getAttribute("banner_color");
    }

    @Nullable
    public String getAccentColor() {
        return oAuth2User.getAttribute("accent_color");
    }

    public String getLocale() {
        return oAuth2User.getAttribute("locale");
    }

    public int hashCode() {
        int result = this.getName().hashCode();
        result = 31 * result + this.getAuthorities().hashCode();
        result = 31 * result + this.getAttributes().hashCode();
        return result;
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: [");
        sb.append(this.getName());
        sb.append("], Granted Authorities: [");
        sb.append(this.getAuthorities());
        sb.append("], User Attributes: [");
        sb.append(this.getAttributes());
        sb.append("]");
        return sb.toString();
    }

    @SuppressWarnings("SimplifiableConditionalExpression")
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            DiscordOAuthUser that = (DiscordOAuthUser) obj;
            if (!this.getName().equals(that.getName())) {
                return false;
            } else {
                return !this.getAuthorities().equals(that.getAuthorities()) ? false : this.getAttributes().equals(that.getAttributes());
            }
        } else {
            return false;
        }
    }
}
