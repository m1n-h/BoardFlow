package com.github.m1n_h.BoardFlow.http;

import com.github.m1n_h.BoardFlow.model.User;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionManager {

    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private static final int DEFAULT_MAX_INACTIVE_INTERVAL = 1800; // 1800초

    private static final ScheduledExecutorService cleanerScheduler =
            Executors.newSingleThreadScheduledExecutor(runnable -> {
                Thread thread = new Thread(runnable, "SessionCleanerThread");
                thread.setDaemon(true);
                return thread;
            });

    static {
        cleanerScheduler.scheduleAtFixedRate(
                SessionManager::cleanExpiredSessions,
                5,
                5,
                TimeUnit.MINUTES
        );
    }

    public static String createSession(Object value) {
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, value, DEFAULT_MAX_INACTIVE_INTERVAL);
        sessions.put(sessionId, session);
        return sessionId;
    }

    public static Object getSession(String sessionId) {
        if (sessionId == null) return null;

        Session session = (Session) sessions.get(sessionId);
        if (session == null) return null;

        if (session.isExpired()) {
            sessions.remove(sessionId);
            return null;
        }

        session.access();
        return session.getAttribute();
    }

    public static void removeSession(String sessionId) {
        if (sessionId != null) sessions.remove(sessionId);
    }

    public static boolean isValidSession(String sessionId) {
        return sessionId != null && sessions.containsKey(sessionId);
    }

    public static User getUserBySessionId(String sessionId) {
        if (!isValidSession(sessionId)) return null;

        Session session = sessions.get(sessionId);
        Object attribute = session.getAttribute();

        if (attribute instanceof User) return (User) attribute;

        return null;
    }

    private static void cleanExpiredSessions() {
        int removedCount = 0;
        for (Map.Entry<String, Session> entry : sessions.entrySet()) {
            if (entry.getValue().isExpired()) {
                sessions.remove(entry.getKey());
                removedCount++;
            }
        }
        if (removedCount > 0) System.out.println("[SessionCleaner] Expired sessions cleaned: " + removedCount);
    }

}
