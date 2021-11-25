package de.blutmondgilde.unity.data.discordapi;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.blutmondgilde.unity.service.DiscordBotService;
import de.blutmondgilde.unity.view.component.DiscordServer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

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

    public Optional<String> getIconUrl() {
        if (icon == null) return Optional.empty();
        if (icon.isEmpty()) return Optional.empty();
        return Optional.of("https://cdn.discordapp.com/icons/" + id + "/" + icon + ".png");
    }

    public VerticalLayout createComponent(DiscordBotService discordBotService) {
        return new DiscordServer(this, discordBotService);
    }
}
