package de.blutmondgilde.unity.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PageTitle("Server Settings")
@Route(value = "server-settings", layout = DefaultLayout.class)
@PermitAll
public class DiscordServerConfiguration extends VerticalLayout {

    public DiscordServerConfiguration() {

    }
}
