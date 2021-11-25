
package de.blutmondgilde.unity.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.blutmondgilde.unity.data.AvatarType;
import de.blutmondgilde.unity.data.discordapi.Guild;
import de.blutmondgilde.unity.service.DiscordAPIHelper;
import de.blutmondgilde.unity.service.DiscordEventService;
import de.blutmondgilde.unity.service.SecurityService;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import java.util.Arrays;
import java.util.List;

/**
 * Application main class that is hidden to user before authentication.
 */
@PageTitle("Home")
@Route(layout = DefaultLayout.class)
@PermitAll
public class MainView extends VerticalLayout {
    final SecurityService securityService;
    final DiscordAPIHelper discordAPIHelper;
    final DiscordEventService discordEventService;

    MainView(SecurityService securityService, DiscordEventService discordEventService) {
        this.securityService = securityService;
        this.discordEventService = discordEventService;
        this.discordAPIHelper = new DiscordAPIHelper(securityService);
    }

    private final FlexLayout guilds = new FlexLayout();

    @PostConstruct
    public void init() {
        Div div = new Div();
        Paragraph name = new Paragraph("Hallo " + securityService.getAuthenticatedUser().getName());
        name.addClassName("font-size-xxl");

        div.add(securityService.getAuthenticatedUser().getAvatarImage(AvatarType.WebP), name);

        Div data = new Div();
        data.setText("Attribute: " + securityService.getAuthenticatedUser().toString());

        guilds.add(new Paragraph("Loading Guilds..."));
        guilds.setJustifyContentMode(JustifyContentMode.AROUND);
        guilds.setAlignItems(Alignment.STRETCH);
        guilds.setFlexDirection(FlexLayout.FlexDirection.ROW);

        discordAPIHelper.getGuilds()
            .doOnSuccess(this::updateGuildList)
            .doOnError(this::updateGuildListError)
            .retry(3)
            .subscribe();

        setAlignItems(Alignment.CENTER);
        add(div, data, guilds);
    }

    private void updateGuildListError(Throwable throwable) {
        getUI().ifPresent(ui -> ui.access(() -> {
            this.guilds.removeAll();
            this.guilds.add(new Paragraph("Failed to load your Guilds from the Discord API."));
        }));
    }

    private void updateGuildList(Guild[] guildArray) {
        List<Guild> guildList = Arrays.stream(guildArray).filter(Guild::isOwner).toList();
        getUI().ifPresent(ui -> ui.access(() -> {
            this.guilds.removeAll();

            guildList.forEach(guild -> {
                VerticalLayout guildComponent = guild.createComponent(discordEventService);
                this.guilds.add(guildComponent);
            });
        }));
    }


}
