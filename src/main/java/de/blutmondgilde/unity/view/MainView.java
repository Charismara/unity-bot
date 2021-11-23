
package de.blutmondgilde.unity.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.blutmondgilde.unity.SecurityService;
import de.blutmondgilde.unity.data.AvatarType;
import de.blutmondgilde.unity.data.discordapi.Guild;
import de.blutmondgilde.unity.service.DiscordAPIHelper;

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
    SecurityService securityService;
    DiscordAPIHelper discordAPIHelper;

    MainView(SecurityService securityService) {
        this.securityService = securityService;
        this.discordAPIHelper = new DiscordAPIHelper(securityService);
    }

    private final Div guilds = new Div();

    @PostConstruct
    public void init() {
        Div div = new Div();
        Paragraph name = new Paragraph("Hallo " + securityService.getAuthenticatedUser().getName());
        name.addClassName("font-size-xxl");

        div.add(securityService.getAuthenticatedUser().getAvatarImage(AvatarType.WebP), name);

        Div data = new Div();
        data.setText("Attribute: " + securityService.getAuthenticatedUser().getAttributes().toString());

        guilds.add(new Paragraph("Loading Guilds..."));
        discordAPIHelper.getGuilds()
            .doOnSuccess(this::updateGuildList)
            .doOnError(this::updateGuildListError)
            .retry(3)
            .subscribe();

        Button logout = new Button("Logout");
        logout.addClickListener(buttonClickEvent -> securityService.logout());

        setAlignItems(Alignment.CENTER);
        add(div, data, guilds, logout);
    }

    private void updateGuildListError(Throwable throwable) {
        getUI().ifPresent(ui -> ui.access(() -> {
            this.guilds.removeAll();
            this.guilds.add(new Paragraph("Failed to load your Guilds from the Discord API."));
        }));
    }

    private void updateGuildList(Guild[] guildArray) {
        List<Guild> guildList = Arrays.stream(guildArray).filter(Guild::isOwner).toList();
        System.out.println(guildList);
        getUI().ifPresent(ui -> ui.access(() -> {
            this.guilds.removeAll();
            guildList.forEach(guild -> {
                Paragraph name = new Paragraph(guild.getName());
                this.guilds.add(name);
            });
        }));
    }


}
