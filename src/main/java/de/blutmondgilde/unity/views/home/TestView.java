package de.blutmondgilde.unity.views.home;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@Route("test")
@PermitAll
public class TestView extends VerticalLayout {
    public TestView() {
        add("It Works! :D");
    }
}
