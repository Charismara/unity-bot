package de.blutmondgilde.unity.data.jpa;

import de.blutmondgilde.unity.data.DiscordOAuthUser;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Getter
    @Setter
    private Long id;
    @Column(length = 33)
    @Getter
    @Setter
    @NotNull
    private String username;
    @Getter
    @Setter
    private String avatarHash;
    @Column(length = 5)
    @Getter
    @Setter
    @NotNull
    @Length(max = 5)
    private String discriminator;
    @Getter
    @Setter
    private String bannerHash;
    @Getter
    @Setter
    private String bannerColor;
    @Getter
    @Setter
    private String accentColor;
    @Getter
    @Setter
    private String locale;
    @Getter
    @Setter
    @Email
    private String email;

    public static User from(DiscordOAuthUser discordOAuthUser) {
        User user = new User();
        user.setId(discordOAuthUser.getDiscordId());
        user.setAccentColor(discordOAuthUser.getAccentColor());
        user.setAvatarHash(discordOAuthUser.getAvatarHash());
        user.setUsername(discordOAuthUser.getName());
        user.setBannerColor(discordOAuthUser.getBannerColor());
        user.setBannerHash(discordOAuthUser.getBannerHash());
        user.setDiscriminator(discordOAuthUser.getDiscriminator());
        user.setEmail(discordOAuthUser.getEMail());
        user.setLocale(discordOAuthUser.getLocale());
        return user;
    }
}
