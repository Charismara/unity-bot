package de.blutmondgilde.unity.data.jpa.guild;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "channel_info")
@Entity
public class ChannelInfo {
    @Id
    @Getter
    @Setter
    private Long channelId;
    @Getter
    @Setter
    private Long settingsId;
    @Getter
    @Setter
    private long ownerId;
    @Getter
    @Setter
    private int remainingDeleteTimer;
    @Getter
    @Setter
    private int remainingTransferTimer;
}