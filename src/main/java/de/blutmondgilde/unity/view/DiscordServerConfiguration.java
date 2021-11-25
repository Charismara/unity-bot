package de.blutmondgilde.unity.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.blutmondgilde.unity.data.jpa.GuildSettingsRepository;
import de.blutmondgilde.unity.service.DiscordBotService;
import de.blutmondgilde.unity.service.SecurityService;
import de.blutmondgilde.unity.view.component.VerticalPagedTabs;
import de.blutmondgilde.unity.view.layout.DefaultLayout;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.vaadin.gatanaso.MultiselectComboBox;
import org.vaadin.tabs.PagedTabs;

import javax.annotation.security.PermitAll;
import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@PageTitle("Server Settings")
@Route(value = "server-settings", layout = DefaultLayout.class)
@PermitAll
@Slf4j
public class DiscordServerConfiguration extends HorizontalLayout implements HasUrlParameter<String> {
    private final DiscordBotService discordBotService;
    private final SecurityService securityService;
    private final GuildSettingsRepository guildSettingsRepository;
    private Guild guild;

    public DiscordServerConfiguration(DiscordBotService discordBotService, SecurityService securityService, GuildSettingsRepository guildSettingsRepository) {
        this.discordBotService = discordBotService;
        this.securityService = securityService;
        this.guildSettingsRepository = guildSettingsRepository;
        setWidthFull();
    }

    private void initializeContent() {
        VerticalLayout container = new VerticalLayout();
        PagedTabs tabs = new VerticalPagedTabs(container);
        tabs.add("Statistics", createStatsContainer(), false);
        tabs.add("Polls", createStatsContainer(), false);
        tabs.add("Reaction Rolls", createStatsContainer(), false);
        tabs.add("Temporary Channels", createStatsContainer(), false);

        if (securityService.getAuthenticatedUser().getDiscordId() == guild.getOwnerIdLong()) {
            tabs.add("Panel Settings", createPanelSettings(), false);
        }

        add(tabs, container);
    }

    private VerticalLayout createStatsContainer() {
        VerticalLayout layout = new VerticalLayout();
        layout.addClassNames("box l radius", "contrast-5pct");

        return layout;
    }

    private VerticalLayout createPanelSettings() {
        VerticalLayout layout = new VerticalLayout();
        layout.addClassNames("box l radius", "contrast-5pct");
        layout.setWidthFull();

        Span title = new Span("Panel Settings");
        title.addClassNames("header-text", "font-size-xxl");
        layout.add(title);

        Hr titleLine = new Hr();
        layout.add(titleLine);

        MultiselectComboBox<Role> allowedRolesList = new MultiselectComboBox<>();
        allowedRolesList.setWidthFull();
        allowedRolesList.setLabel("Allowed Roles");
        allowedRolesList.setPlaceholder("add...");
        allowedRolesList.setItems(guild.getRoles());
        allowedRolesList.setClearButtonVisible(true);
        allowedRolesList.setItemLabelGenerator(Role::getName);

        allowedRolesList.setRenderer(TemplateRenderer.<Role>of("<div style=\"background-color: [[item.color]]\" class=\"box radius l\">[[item.name]]</div>")
            .withProperty("color", role -> {
                Color color = role.getColor();
                if (color == null) {
                    color = Color.DARK_GRAY;
                }
                return "rgb(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")";
            })
            .withProperty("name", Role::getName)
        );

        allowedRolesList.addSelectionListener(multiSelectionEvent -> {
            this.guildSettingsRepository.findById(guild.getId()).ifPresent(guildSettings -> {
                //Update selection
                guildSettings.setAllowedRoles(multiSelectionEvent.getAllSelectedItems().stream().map(Role::getIdLong).toList());
                this.guildSettingsRepository.save(guildSettings);
            });
        });

        this.guildSettingsRepository.findById(guild.getId()).ifPresent(guildSettings -> {
            List<Role> selectedRoles = guildSettings.getAllowedRoles()
                .stream()
                .map(id -> guild.getRoleById(id))
                .filter(Objects::nonNull)
                .toList();
            allowedRolesList.setValue(new HashSet<>(selectedRoles));
        });

        layout.add(allowedRolesList);

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
