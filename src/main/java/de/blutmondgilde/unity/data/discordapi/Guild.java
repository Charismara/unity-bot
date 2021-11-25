package de.blutmondgilde.unity.data.discordapi;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

    public VerticalLayout createComponent() {
        VerticalLayout root = new VerticalLayout();
        root.setPadding(false);
        root.setMargin(true);
        root.setMinWidth(180F * 2, Unit.PIXELS);
        root.setMinHeight(100F * 2, Unit.PIXELS);
        root.addClassNames("guildComponentContainer", "box l radius");

        VerticalLayout imageContainer = new VerticalLayout();
        imageContainer.setWidthFull();
        imageContainer.setHeightFull();
        imageContainer.addClassNames("guildImageContainer", "box l radius");
        imageContainer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        imageContainer.setAlignItems(FlexComponent.Alignment.CENTER);
        if (getIconUrl().isPresent()) {
            imageContainer.getStyle().set("background-image", "url(\"" + getIconUrl().get() + "\")");
        } else {
            imageContainer.addClassName("contrast-5pct");
        }

        Div serverName = new Div();
        serverName.addClassName("guildName");
        serverName.setText(this.name);
        imageContainer.add(serverName);

        FlexLayout blurOnHover = new FlexLayout();
        blurOnHover.setWidthFull();
        blurOnHover.setHeightFull();
        blurOnHover.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        blurOnHover.setAlignItems(FlexComponent.Alignment.CENTER);
        blurOnHover.addClassName("guildBlurOnHover");

        //TODO change to "Settings" if bot is already on this Server
        Span clickText = new Span("Setup");
        clickText.addClassNames("box l radius","guildSetupButton");
        blurOnHover.add(clickText);

        root.add(imageContainer, blurOnHover);
        return root;
    }
}
