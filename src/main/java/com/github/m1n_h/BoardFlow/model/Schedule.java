package com.github.m1n_h.BoardFlow.model;

import java.time.LocalDateTime;

public class Schedule {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String writer;
    private boolean completed;

    public Schedule(Long id, String title, String content, LocalDateTime startDateTime, LocalDateTime endDateTime, String writer) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.writer = writer;
        this.completed = false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getStartDateTime() { return startDateTime; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }

    public LocalDateTime getEndDateTime() { return endDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }

    public String getWriter() { return writer; }
    public void setWriter(String writer) { this.writer = writer; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

}
