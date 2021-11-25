package de.blutmondgilde.unity.view.component;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.tabs.Tabs;
import org.vaadin.tabs.PagedTabs;

public class VerticalPagedTabs extends PagedTabs {
    public VerticalPagedTabs(HasComponents componentContainer) {
        super(componentContainer);
        getContent().setOrientation(Tabs.Orientation.VERTICAL);
        getContent().setWidth(200, Unit.PIXELS);
    }
}
