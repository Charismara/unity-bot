package de.blutmondgilde.unity.data.jpa.guild;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Embeddable
public class GuildChannelTemplate implements Serializable {
    @Setter
    @Getter
    private String channelNameTemplate = "#{index} {username}'s Channel";
    @Setter
    @Getter
    @Min(message = "The Channel bitrate has to be atleast 8000 bits", value = 8000)
    private int bitRate = 96000;
    @Setter
    @Getter
    private int userLimit = -1;
    @Setter
    @Getter
    private int keepAlive = 0;
    @Setter
    @Getter
    private int ownershipDelay = 1;
}