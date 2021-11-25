package de.blutmondgilde.unity.view.component;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.blutmondgilde.unity.data.discordapi.Guild;

public class DiscordServer extends VerticalLayout {
    private final Guild guild;

    public DiscordServer(Guild guild) {
        this.guild = guild;
        setPadding(false);
        setMargin(true);
        setMinWidth(180F * 2, Unit.PIXELS);
        setMinHeight(100F * 2, Unit.PIXELS);
        addClassNames("guildComponentContainer", "box l radius");
        add(imageLayout(),hoverLayout());
    }

    private VerticalLayout imageLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();
        layout.setHeightFull();
        layout.addClassNames("guildImageContainer", "box l radius");
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        if (guild.getIconUrl().isPresent()) {
            layout.getStyle().set("background-image", "url(\"" + guild.getIconUrl().get() + "\")");
        } else {
            layout.addClassName("contrast-5pct");
        }

        Div serverName = new Div();
        serverName.addClassName("guildName");
        serverName.setText(guild.getName());
        layout.add(serverName);

        return layout;
    }

    private FlexLayout hoverLayout() {
        FlexLayout layout = new FlexLayout();
        layout.setWidthFull();
        layout.setHeightFull();
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.addClassName("guildBlurOnHover");

        //TODO change to "Settings" if bot is already on this Server
        Span clickText = new Span("Setup");
        clickText.addClassNames("box l radius", "guildSetupButton");
        layout.add(clickText);
        layout.addClickListener(flexLayoutClickEvent -> {
            flexLayoutClickEvent.getSource().getUI().ifPresent(ui -> {
                ui.getPage().executeJs("window.open(\"https://discord.com/oauth2/authorize?client_id=907572774439112754&scope=bot+applications.commands&permissions=8&guild_id=" + guild.getId() + "\",''," +
                    "\"width=400,height=700\")");
            });
        });

        return layout;
    }
}
