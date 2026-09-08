package com.github.m1n_h.BoardFlow.model;

public class Schedule {
    private Long id;
    private String userId;
    private String title;
    private String scheduledAt;

    public Schedule(Long id, String userId, String title, String scheduledAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.scheduledAt = scheduledAt;
    }

    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getScheduledAt() { return scheduledAt; }
}
