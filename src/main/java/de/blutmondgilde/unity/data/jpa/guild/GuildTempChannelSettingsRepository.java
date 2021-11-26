package de.blutmondgilde.unity.data.jpa.guild;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuildTempChannelSettingsRepository extends JpaRepository<GuildTempChannelSettings, Long> {
    List<GuildTempChannelSettings> findByGuildId(long guildId);

}