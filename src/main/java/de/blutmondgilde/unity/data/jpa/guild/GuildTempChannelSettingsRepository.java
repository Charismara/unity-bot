package de.blutmondgilde.unity.data.jpa.guild;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuildTempChannelSettingsRepository extends JpaRepository<GuildTempChannelSettings, Long> {
    List<GuildTempChannelSettings> findByGuildId(long guildId);

    Optional<GuildTempChannelSettings> findByChannelId(long channelId);

}