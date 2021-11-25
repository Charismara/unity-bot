package de.blutmondgilde.unity.data.jpa.stats;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "guild_user_amount")
@Entity
@IdClass(GuildStatsId.class)
public class GuildUserAmount implements Comparable<GuildUserAmount> {
    @Id
    @Setter
    @Getter
    private Long guildId;
    @Id
    @Setter
    @Getter
    private Date creationDate;
    @Setter
    @Getter
    private int userCount;

    @Override
    public int compareTo(@NotNull GuildUserAmount o) {
        return creationDate.compareTo(getCreationDate());
    }
}