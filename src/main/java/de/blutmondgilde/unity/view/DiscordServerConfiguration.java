package de.blutmondgilde.unity.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.blutmondgilde.unity.service.DiscordBotService;
import de.blutmondgilde.unity.service.SecurityService;
import de.blutmondgilde.unity.view.component.VerticalPagedTabs;
import net.dv8tion.jda.api.entities.Guild;
import org.vaadin.tabs.PagedTabs;

import javax.annotation.security.PermitAll;
import java.util.Optional;

@PageTitle("Server Settings")
@Route(value = "server-settings", layout = DefaultLayout.class)
@PermitAll
public class DiscordServerConfiguration extends HorizontalLayout implements HasUrlParameter<String> {
    private final DiscordBotService discordBotService;
    private final SecurityService securityService;
    private Guild guild;

    public DiscordServerConfiguration(DiscordBotService discordBotService, SecurityService securityService) {
        this.discordBotService = discordBotService;
        this.securityService = securityService;
        setWidthFull();
    }

    private void initializeContent() {
        VerticalLayout container = new VerticalLayout();
        PagedTabs tabs = new VerticalPagedTabs(container);
        tabs.add("Statistics", createStatsContainer(), false);
        tabs.add("Polls", createStatsContainer(), false);
        tabs.add("Reaction Rolls", createStatsContainer(), false);
        tabs.add("Temporary Channels", createStatsContainer(), false);
        add(tabs, container);
    }

    private VerticalLayout createStatsContainer() {
        VerticalLayout layout = new VerticalLayout();
        layout.addClassNames("box l radius", "contrast-5pct");

        return layout;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        Optional<Guild> optionalGuild = discordBotService.getGuild(s);
        if (optionalGuild.isEmpty()) {
            beforeEvent.forwardTo(MainView.class);
        } else {
            this.guild = optionalGuild.get();
            initializeContent();
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {

    }
}
