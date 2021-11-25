package de.blutmondgilde.unity.data.jpa.stats;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuildUserAmountRepository extends JpaRepository<GuildUserAmount, GuildStatsId> {
    List<GuildUserAmount> findByGuildId(Long guildId);

}