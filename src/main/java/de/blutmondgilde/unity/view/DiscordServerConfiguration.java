package de.blutmondgilde.unity.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.crud.CrudGrid;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.blutmondgilde.unity.data.jpa.guild.GuildSettingsRepository;
import de.blutmondgilde.unity.data.jpa.guild.GuildTempChannelSettings;
import de.blutmondgilde.unity.data.jpa.guild.GuildTempChannelSettingsRepository;
import de.blutmondgilde.unity.data.jpa.stats.GuildUserAmount;
import de.blutmondgilde.unity.data.jpa.stats.GuildUserStatsRepository;
import de.blutmondgilde.unity.service.DiscordBotService;
import de.blutmondgilde.unity.service.SecurityService;
import de.blutmondgilde.unity.view.component.GuildTempChannelSettingsDataProvider;
import de.blutmondgilde.unity.view.component.VerticalPagedTabs;
import de.blutmondgilde.unity.view.layout.DefaultLayout;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.vaadin.gatanaso.MultiselectComboBox;
import org.vaadin.tabs.PagedTabs;

import javax.annotation.security.PermitAll;
import java.awt.Color;
import java.util.ArrayList;
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
    private final GuildUserStatsRepository guildUserStatsRepository;
    private final GuildTempChannelSettingsRepository guildTempChannelSettingsRepository;
    private Guild guild;

    public DiscordServerConfiguration(DiscordBotService discordBotService, SecurityService securityService, GuildSettingsRepository guildSettingsRepository,
                                      GuildUserStatsRepository guildUserStatsRepository, GuildTempChannelSettingsRepository guildTempChannelSettingsRepository) {
        this.discordBotService = discordBotService;
        this.securityService = securityService;
        this.guildSettingsRepository = guildSettingsRepository;
        this.guildUserStatsRepository = guildUserStatsRepository;
        this.guildTempChannelSettingsRepository = guildTempChannelSettingsRepository;
        setWidthFull();
    }

    private void initializeContent() {
        VerticalLayout container = new VerticalLayout();
        PagedTabs tabs = new VerticalPagedTabs(container);
        tabs.add("Statistics", createStatsOverview(), false);
        tabs.add("Polls", createEmptyContainer(), false);
        tabs.add("Reaction Rolls", createEmptyContainer(), false);
        tabs.add("Temporary Channels", createTemporaryChannelSettings(), false);

        if (securityService.getAuthenticatedUser().getDiscordId() == guild.getOwnerIdLong()) {
            tabs.add("Panel Settings", createPanelSettings(), false);
        }

        add(tabs, container);
    }

    private VerticalLayout createEmptyContainer() {
        VerticalLayout layout = new VerticalLayout();
        layout.addClassNames("box l radius", "contrast-5pct");

        Span title = new Span("Comming Soon");
        title.addClassNames("header-text", "font-size-xxl");
        layout.add(title);

        Hr titleLine = new Hr();
        layout.add(titleLine);

        return layout;
    }

    private VerticalLayout createTemporaryChannelSettings() {
        VerticalLayout layout = new VerticalLayout();
        layout.addClassNames("box l radius", "contrast-5pct");

        Span title = new Span("Comming Soon");
        title.addClassNames("header-text", "font-size-xxl");
        layout.add(title);

        Hr titleLine = new Hr();
        layout.add(titleLine);

        Crud<GuildTempChannelSettings> crud = new Crud<>(GuildTempChannelSettings.class, createGrid(), createCrudEditor());

        GuildTempChannelSettingsDataProvider dataProvider = new GuildTempChannelSettingsDataProvider(this.guildTempChannelSettingsRepository, this.guild.getIdLong());
        crud.setDataProvider(dataProvider);
        crud.addDeleteListener(deleteEvent -> {
            //TODO delete channel
            this.guildTempChannelSettingsRepository.delete(deleteEvent.getItem());
        });
        crud.addSaveListener(saveEvent -> {
            //TODO create channel
            this.guildTempChannelSettingsRepository.save(saveEvent.getItem());
        });


        layout.add(crud);

        Html total = new Html("<span>Total: <b>" + this.guildTempChannelSettingsRepository.findByGuildId(this.guild.getIdLong()).size() + "</b> employees</span>");

        Button button = new Button("New", VaadinIcon.PLUS.create());
        button.addClickListener(event -> crud.edit(new GuildTempChannelSettings(), Crud.EditMode.NEW_ITEM));
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout toolbar = new HorizontalLayout(total, button);
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);
        toolbar.setFlexGrow(1, toolbar);
        toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        toolbar.setSpacing(false);

        crud.setToolbar(toolbar);

        return layout;
    }


    private VerticalLayout createStatsOverview() {
        VerticalLayout layout = new VerticalLayout();
        layout.addClassNames("box l radius", "contrast-5pct");

        Span title = new Span("Statistics");
        title.addClassNames("header-text", "font-size-xxl");
        layout.add(title);

        Hr titleLine = new Hr();
        layout.add(titleLine);

        Chart userAmountChart = new Chart(ChartType.LINE);
        userAmountChart.getConfiguration().setTitle("Total Users");
        userAmountChart.getConfiguration().setSubTitle("Shows the total amount of Users on this Server");
        userAmountChart.getConfiguration().getyAxis().setTitle("users");
        userAmountChart.getConfiguration().getyAxis().setAllowDecimals(false);
        userAmountChart.getConfiguration().getxAxis().setTitle("day");
        userAmountChart.getConfiguration().getxAxis().setAllowDecimals(false);

        DataSeries userTimeDataSeries = new DataSeries("Total Users");
        this.guildUserStatsRepository.findById(this.guild.getIdLong()).ifPresent(guildUserStats -> {

            ArrayList<GuildUserAmount> data = new ArrayList<>(guildUserStats.getUserAmount());
            data.sort(GuildUserAmount::compareTo);
            //if(data.size()>)

            for (int i = 0; i < data.size(); i++) {
                userTimeDataSeries.add(new DataSeriesItem(i + 1, data.get(i).getUserCount()));
            }
        });

        userAmountChart.getConfiguration().setSeries(userTimeDataSeries);
        userAmountChart.setWidthFull();
        userAmountChart.addClassName("styled-chart");
        layout.add(userAmountChart);

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

    private CrudEditor<GuildTempChannelSettings> createCrudEditor() {
        TextField channelName = new TextField("Channel Name");

        FormLayout form = new FormLayout();
        form.add(channelName);

        Binder<GuildTempChannelSettings> binder = new Binder<>(GuildTempChannelSettings.class);
        binder.forField(channelName).asRequired().bind(GuildTempChannelSettings::getChannelName, GuildTempChannelSettings::setChannelName);

        return new BinderCrudEditor<>(binder, form);
    }

    private CrudGrid<GuildTempChannelSettings> createGrid() {
        CrudGrid<GuildTempChannelSettings> grid = new CrudGrid<>(GuildTempChannelSettings.class, false);
        grid.setSortableColumns();

        List<String> keys = List.of("channelName", "vaadin-crud-edit-column");

        grid.getColumns().forEach(column -> {
            if (!keys.contains(column.getKey())) {
                grid.removeColumn(column);
            }
        });

        return grid;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {

    }
}
