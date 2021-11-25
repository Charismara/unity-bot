package de.blutmondgilde.unity.data.jpa.stats;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Table(name = "guild_user_stats")
@Entity
public class GuildUserStats {
    @Id
    @Getter
    @Setter
    private Long guildId;

    @OneToMany(mappedBy = "guildId")
    @Getter
    @Setter
    private List<GuildUserAmount> userAmount;
}