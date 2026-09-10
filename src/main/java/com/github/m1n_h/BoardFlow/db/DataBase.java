package com.github.m1n_h.BoardFlow.db;

import com.github.m1n_h.BoardFlow.model.Article;
import com.github.m1n_h.BoardFlow.model.Schedule;
import com.github.m1n_h.BoardFlow.model.User;

import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class DataBase {

    private static final Map<String, User> users = new ConcurrentHashMap<>();
    private static final Map<Long, Article> articles = new ConcurrentHashMap<>();
    private static final Map<Long, Schedule> schedules = new ConcurrentHashMap<>();

    private static final AtomicLong articleIdGenerator = new AtomicLong(1);
    private static final AtomicLong scheduleIdGenerator = new AtomicLong(1);

    public static void addUser(User user) { users.put(user.getUserId(), user); }
    public static User findUserById(String userId) {
        if (userId == null) return null;
        return users.get(userId);
    }
    public static User getUserById(String userId) { return users.get(userId); }
    public static Collection<User> findAll() { return users.values(); }


    public static Article addArticle(String title, String content, String writer) {
        Long ArticleId = articleIdGenerator.getAndIncrement();
        String createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Article article = new Article(ArticleId, title, content, writer, createdAt);
        articles.put(ArticleId, article);
        return article;
    }

    public static Article findArticleById(Long articleId) { return articles.get(articleId); }
    public static Collection<Article> findAllArticles() { return articles.values(); }
    public static void deleteArticle(Long articleId) { articles.remove(articleId); }

    public static void updateArticle(Long articleId, String newTitle, String newContent) {
        Article article = articles.get(articleId);
        if (article != null) {
            article.setTitle(newTitle);
            article.setContent(newContent);
        }
    }


    public static Schedule addSchedule(String title, String content, LocalDateTime start, LocalDateTime end, String writer) {
        Long scheduleId = scheduleIdGenerator.getAndIncrement();

        Schedule schedule = new Schedule(scheduleId, title, content, start, end, writer);
        schedules.put(scheduleId, schedule);
        return schedule;
    }

    public static Schedule findScheduleById(Long scheduleId) { return schedules.get(scheduleId); }
    public static Collection<Schedule> findAllSchedules() { return schedules.values(); }
    public static void deleteSchedule(Long scheduleId) { schedules.remove(scheduleId); }

    public static void updateSchedule(Long scheduleId, String newTitle, String newContent, LocalDateTime start, LocalDateTime end) {
        Schedule schedule = schedules.get(scheduleId);
        if (schedule != null) {
            schedule.setTitle(newTitle);
            schedule.setContent(newContent);
            schedule.setStartDateTime(start);
            schedule.setEndDateTime(end);
        }
    }

}
