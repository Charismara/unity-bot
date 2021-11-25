package de.blutmondgilde.unity.data.jpa.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public class GuildStatsId implements Serializable {
    @Setter
    @Getter
    private long guildId;
    @Setter
    @Getter
    private Date creationDate;
}
