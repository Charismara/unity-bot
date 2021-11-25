package de.blutmondgilde.unity.view;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.blutmondgilde.unity.service.DiscordEventService;
import de.blutmondgilde.unity.view.component.VerticalPagedTabs;
import org.vaadin.tabs.PagedTabs;

import javax.annotation.security.PermitAll;

@PageTitle("Server Settings")
@Route(value = "server-settings", layout = DefaultLayout.class)
@PermitAll
public class DiscordServerConfiguration extends HorizontalLayout {
    private final DiscordEventService discordEventService;

    public DiscordServerConfiguration(DiscordEventService discordEventService) {
        this.discordEventService = discordEventService;
        setWidthFull();
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
}
