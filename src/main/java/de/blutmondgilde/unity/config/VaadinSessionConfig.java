package de.blutmondgilde.unity.config;

import com.vaadin.flow.server.CustomizedSystemMessages;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.SessionDestroyEvent;
import com.vaadin.flow.server.SessionDestroyListener;
import com.vaadin.flow.server.SystemMessages;
import com.vaadin.flow.server.SystemMessagesInfo;
import com.vaadin.flow.server.SystemMessagesProvider;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class VaadinSessionConfig implements VaadinServiceInitListener, SystemMessagesProvider, SessionDestroyListener {
    private final String relativeSessionExpiredUrl;

    VaadinSessionConfig(ServerProperties serverProperties) {
        relativeSessionExpiredUrl = UriComponentsBuilder.fromPath(serverProperties.getServlet().getContextPath()).path("session-expired").build().toUriString();
    }

    @Override
    public void sessionDestroy(SessionDestroyEvent e) {
        try {
            e.getSession().getSession().invalidate();
        } catch (Exception ignore) {
        }
    }

    @Override
    public SystemMessages getSystemMessages(SystemMessagesInfo systemMessagesInfo) {
        var message = new CustomizedSystemMessages();
        message.setSessionExpiredURL(relativeSessionExpiredUrl);
        return message;
    }

    @Override
    public void serviceInit(ServiceInitEvent e) {
        e.getSource().setSystemMessagesProvider(this);
        e.getSource().addSessionDestroyListener(this);
    }
}
