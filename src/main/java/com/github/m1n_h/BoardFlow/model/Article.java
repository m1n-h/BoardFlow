package com.github.m1n_h.BoardFlow.model;

public class Article {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private String createdAt;

    public Article(Long id, String title, String content, String writer, String createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getWriter() { return writer; }
    public String getCreatedAt() { return createdAt; }

    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
}
