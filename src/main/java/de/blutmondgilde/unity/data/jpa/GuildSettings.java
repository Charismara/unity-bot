package de.blutmondgilde.unity.data.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Table(name = "guild_settings")
@Entity
public class GuildSettings {
    @Setter
    @Getter
    @Id
    private String id;
    @Setter
    @Getter
    private Long owner;
    @Setter
    @Getter
    @ElementCollection
    private List<Long> allowedRoles;
}