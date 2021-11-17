package de.blutmondgilde.unity.security;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionRepositoryListener implements HttpSessionListener {
    private final SessionRepository sessionRepository;

    public SessionRepositoryListener(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        sessionRepository.add(se.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        sessionRepository.remove(se.getSession());
    }
}
