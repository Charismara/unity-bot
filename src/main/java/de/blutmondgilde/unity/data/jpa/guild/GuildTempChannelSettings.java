package de.blutmondgilde.unity.data.jpa.guild;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Table(name = "guild_temp_channel_settings")
@Entity
public class GuildTempChannelSettings {
    @GeneratedValue
    @Id
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private long guildId;
    @Getter
    @Setter
    private long channelId;
    @Getter
    @Setter
    private String channelName;
    @Embedded
    @Getter
    @Setter
    private GuildChannelTemplate channelTemplate;
    @OneToMany(mappedBy = "channelId")
    @Getter
    @Setter
    private List<ChannelInfo> channelInfos;
}