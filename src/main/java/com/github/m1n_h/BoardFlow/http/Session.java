package com.github.m1n_h.BoardFlow.http;

public class Session {
    private final String id;
    private Object attribute;
    private long lastAccessedTime;
    private final int maxInactiveInterval;

    public Session(String id, Object attribute, int maxInactiveIntervalSeconds) {
        this.id = id;
        this.attribute = attribute;
        this.maxInactiveInterval = maxInactiveIntervalSeconds;
        this.lastAccessedTime = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public Object getAttribute() { return attribute; }

    public void setAttribute(Object attribute) { this.attribute = attribute; }
    public void access() { this.lastAccessedTime = System.currentTimeMillis(); }

    public boolean isExpired() {
        long now = System.currentTimeMillis();
        long inactiveTime = (now - lastAccessedTime) / 1000;
        return inactiveTime > maxInactiveInterval;
    }

}
