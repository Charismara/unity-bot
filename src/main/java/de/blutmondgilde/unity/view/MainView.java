
package de.blutmondgilde.unity.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.blutmondgilde.unity.data.AvatarType;
import de.blutmondgilde.unity.data.discordapi.Guild;
import de.blutmondgilde.unity.data.jpa.guild.GuildSettings;
import de.blutmondgilde.unity.data.jpa.guild.GuildSettingsRepository;
import de.blutmondgilde.unity.service.DiscordAPIHelper;
import de.blutmondgilde.unity.service.DiscordBotService;
import de.blutmondgilde.unity.service.SecurityService;
import de.blutmondgilde.unity.view.layout.DefaultLayout;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Application main class that is hidden to user before authentication.
 */
@PageTitle("Home")
@Route(layout = DefaultLayout.class)
@PermitAll
public class MainView extends VerticalLayout {
    final SecurityService securityService;
    final DiscordAPIHelper discordAPIHelper;
    final DiscordBotService discordBotService;
    final GuildSettingsRepository guildSettingsRepository;

    MainView(SecurityService securityService, DiscordBotService discordBotService, GuildSettingsRepository guildSettingsRepository) {
        this.securityService = securityService;
        this.discordBotService = discordBotService;
        this.discordAPIHelper = new DiscordAPIHelper(securityService);
        this.guildSettingsRepository = guildSettingsRepository;
    }

    private final FlexLayout guilds = new FlexLayout();

    @PostConstruct
    public void init() {
        Div div = new Div();
        Paragraph name = new Paragraph("Hallo " + securityService.getAuthenticatedUser().getName());
        name.addClassName("font-size-xxl");

        div.add(securityService.getAuthenticatedUser().getAvatarImage(AvatarType.WebP), name);

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
        add(div, guilds);
    }

    private void updateGuildListError(Throwable throwable) {
        getUI().ifPresent(ui -> ui.access(() -> {
            this.guilds.removeAll();
            this.guilds.add(new Paragraph("Failed to load your Guilds from the Discord API."));
        }));
    }

    private void updateGuildList(Guild[] guildArray) {
        List<Guild> guildList = Arrays.stream(guildArray)
            .filter(this::hasAccess)
            .toList();
        getUI().ifPresent(ui -> ui.access(() -> {
            this.guilds.removeAll();

            guildList.forEach(guild -> {
                VerticalLayout guildComponent = guild.createComponent(discordBotService);
                this.guilds.add(guildComponent);
            });
        }));
    }

    private boolean hasAccess(Guild guild) {
        if (guild.isOwner()) return true;
        Optional<net.dv8tion.jda.api.entities.Guild> serviceGuild = discordBotService.getGuild(guild.getId());
        if (serviceGuild.isEmpty()) return false;

        net.dv8tion.jda.api.entities.Guild loadedGuild = serviceGuild.get();
        Member member = loadedGuild.getMemberById(securityService.getAuthenticatedUser().getDiscordId());
        if (member == null) return false;

        Optional<GuildSettings> loadedSettings = this.guildSettingsRepository.findById(guild.getId());
        if (loadedSettings.isEmpty()) return false;

        GuildSettings settings = loadedSettings.get();

        for (Role role : member.getRoles()) {
            if (settings.getAllowedRoles().contains(role.getIdLong())) {
                return true;
            }
        }

        return false;
    }
}
