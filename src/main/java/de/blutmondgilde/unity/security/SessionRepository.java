package de.blutmondgilde.unity.security;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class SessionRepository {
    private final Set<HttpSession> sessions = new HashSet<>();

    public synchronized void add(HttpSession session) {
        sessions.add(session);
    }

    public synchronized void remove(HttpSession session) {
        sessions.remove(session);
    }

    public synchronized void invalidate(Predicate<HttpSession> predicate) {
        Set.copyOf(sessions).stream().filter(predicate).forEach(HttpSession::invalidate);
    }
}
