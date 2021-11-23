package de.blutmondgilde.unity.data.discordapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Guild {
    @Getter
    @Setter
    private String id, name, icon, permissions_new;
    @Getter
    @Setter
    private boolean owner;
    @Getter
    @Setter
    private List<String> features;
}
