package com.github.m1n_h.BoardFlow.controller;

import com.github.m1n_h.BoardFlow.db.DataBase;
import com.github.m1n_h.BoardFlow.http.HttpRequest;
import com.github.m1n_h.BoardFlow.http.HttpResponse;
import com.github.m1n_h.BoardFlow.http.SessionManager;
import com.github.m1n_h.BoardFlow.model.Article;
import com.github.m1n_h.BoardFlow.model.Schedule;
import com.github.m1n_h.BoardFlow.model.User;

import java.io.IOException;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ScheduleController implements Controller {

    @Override
    public void process(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();
        String method = request.getMethod();

        try {
            if ("GET".equalsIgnoreCase(method) &&
                    ("/list".equals(path) || "/schedule/list".equals(path)
                            || "/list.html".equals(path) || "/schedule/list.html".equals(path))
            ) {
                showList(request, response);
            } else if ("GET".equalsIgnoreCase(method) &&
                    ("/detail".equals(path) || "/schedule/detail".equals(path)
                            || "/detail.html".equals(path) || "/schedule/detail.html".equals(path))
            ) {
                showDetail(request, response);
            } else if ("/write".equals(path) || "/schedule/write".equals(path)
                    || "/write.html".equals(path) || "/schedule/write.html".equals(path)) {
                if ("GET".equalsIgnoreCase(method)) {
                    showForm(request, response);
                } else if ("POST".equalsIgnoreCase(method)) {
                    createSchedule(request, response);
                }
            } else if ("/modify".equals(path) || "/schedule/modify".equals(path)
                    || "/modify.html".equals(path) || "/schedule/modify.html".equals(path)
            ) {
                if ("GET".equalsIgnoreCase(method)) {
                    showUpdateForm(request, response);
                } else if ("POST".equalsIgnoreCase(method)) {
                    updateSchedule(request, response);
                }
            } else if ("POST".equalsIgnoreCase(method) &&
                    ("/delete".equals(path) || "/schedule/delete".equals(path))
            ) {
                deleteSchedule(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.sendHtml("INTERNAL SERVER ERROR");
        }
    }

    private void showList(HttpRequest request, HttpResponse response) throws IOException {
        Collection<Schedule> schedules = DataBase.findAllSchedules();

        Map<String, Object> model = new HashMap<>();
        model.put("schedule", schedules);

        response.render("static/schedule/list.html", model);
    }

    private void showDetail(HttpRequest request, HttpResponse response) throws IOException {
        String idParam = request.getParam("id");
        if (idParam == null || idParam.isBlank()) {
            response.setStatus(400);
            response.sendHtml("BAD REQUEST: Missing Schedule ID");
            return;
        }

        Long scheduleId;
        try {
            scheduleId = Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            response.setStatus(400);
            response.sendHtml("BAD REQUEST: Invalid Schedule ID format");
            return;
        }

        Schedule schedule = DataBase.findScheduleById(scheduleId);

        if (schedule == null) {
            response.setStatus(404);
            response.sendHtml("NOT FOUND");
            return;
        }

        User user = getLoginUser(request);

        boolean isWriter = user != null && schedule.getWriter().equals(user.getUserName());
        boolean isAdmin = user != null && "관리자".equals(user.getUserName());

        Map<String, Object> model = new HashMap<>();
        model.put("schedule", schedule);
        model.put("id", schedule.getId());
        model.put("title", schedule.getTitle());
        model.put("content", schedule.getContent());
        model.put("start", schedule.getStartDateTime());
        model.put("end", schedule.getEndDateTime());
        model.put("writer", schedule.getWriter());

        if (isWriter || isAdmin) {
            String actionButtons = String.format(
                    "<a href=\"/schedule/modify?id=%d\" class=\"btn btn-sm btn-outline-secondary schedule-update-btn\" data-id=\"%d\">수정</a> " +
                    "<button type=\"button\" onclick=\"deleteSchedule(%d)\" class=\"btn btn-sm btn-outline-danger ms-1 schedule-delete-btn\" data-id=\"%d\">삭제</button>",
                    schedule.getId(), schedule.getId(), schedule.getId(), schedule.getId()
            );
            model.put("actionButtons", actionButtons);
        } else {
            model.put("actionButtons", "");
        }

        response.render("static/schedule/detail.html", model);
    }

    private void showForm(HttpRequest request, HttpResponse response) throws IOException {
        if (!isLoggedIn(request)) {
            response.setStatus(401);
            response.sendHtml("UNAUTHORIZED");
            return;
        }

        response.render("static/schedule/write.html", new HashMap<>());
    }

    private void createSchedule(HttpRequest request, HttpResponse response) throws IOException {
        User user = getLoginUser(request);
        if (user == null) {
            response.setStatus(401);
            response.sendHtml("UNAUTHORIZED");
            return;
        }

        String title = request.getParam("title");
        String content = request.getParam("content");
        String startParam = request.getParam("start");
        String endParam = request.getParam("end");

        if (title == null || title.isBlank() || startParam == null || endParam == null) {
            response.setStatus(400);
            response.sendHtml("INVALID REQUEST");
            return;
        }

        try {
            LocalDateTime start = LocalDateTime.parse(startParam);
            LocalDateTime end = LocalDateTime.parse(endParam);

            DataBase.addSchedule(title, content, start, end, user.getUserName());
            response.sendRedirect("/schedule/list");
        } catch (DateTimeParseException e) {
            response.setStatus(400);
            response.sendHtml("INVALID DATE FORMAT");
        }
    }

    private void showUpdateForm(HttpRequest request, HttpResponse response) throws IOException {
        User user = getLoginUser(request);
        if (user == null) {
            response.setStatus(401);
            response.sendHtml("UNAUTHORIZED");
            return;
        }

        Long scheduleId = Long.parseLong(request.getParam("id"));
        Schedule schedule = DataBase.findScheduleById(scheduleId);

        if (schedule == null) {
            response.setStatus(404);
            response.sendHtml("NOT FOUND");
            return;
        }

        if (!schedule.getWriter().equals(user.getUserName()) && !"관리자".equals(user.getUserName())) {
            response.setStatus(403);
            response.sendHtml("FORBIDDEN");
            return;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("id", schedule.getId());
        model.put("title", schedule.getTitle());
        model.put("content", schedule.getContent());
        model.put("start", schedule.getStartDateTime());
        model.put("end", schedule.getEndDateTime());

        response.render("static/schedule/modify.html", model);
    }

    private void updateSchedule(HttpRequest request, HttpResponse response) throws IOException {
        User user = getLoginUser(request);
        if (user == null) {
            response.setStatus(401);
            response.sendHtml("UNAUTHORIZED");
            return;
        }

        Long scheduleId = Long.parseLong(request.getParam("id"));
        Schedule schedule = DataBase.findScheduleById(scheduleId);

        if (schedule == null) {
            response.setStatus(404);
            response.sendHtml("NOT FOUND");
            return;
        }

        if (!schedule.getWriter().equals(user.getUserName()) && !"관리자".equals(user.getUserName())) {
            response.setStatus(403);
            response.sendHtml("FORBIDDEN");
            return;
        }

        String title = request.getParam("title");
        String content = request.getParam("content");
        String startParam = request.getParam("start");
        String endParam = request.getParam("end");

        try {
            LocalDateTime start = LocalDateTime.parse(startParam);
            LocalDateTime end = LocalDateTime.parse(endParam);

            DataBase.updateSchedule(scheduleId, title, content, start, end);
            response.sendRedirect("/schedule/detail?id=" + schedule.getId());
        } catch (DateTimeParseException e) {
            response.setStatus(400);
            response.sendHtml("INVALID DATE FORMAT");
        }
    }

    private void deleteSchedule(HttpRequest request, HttpResponse response) throws IOException {
        User user = getLoginUser(request);
        if (user == null) {
            response.setStatus(401);
            response.sendHtml("UNAUTHORIZED");
            return;
        }

        Long scheduleId = Long.parseLong(request.getParam("id"));
        Schedule schedule = DataBase.findScheduleById(scheduleId);

        if (schedule == null) {
            response.setStatus(404);
            response.sendHtml("NOT FOUND");
            return;
        }

        boolean isWriter = schedule.getWriter().equals(user.getUserName());
        boolean isAdmin = "관리자".equals(user.getUserName());

        if (!isWriter && !isAdmin) {
            response.setStatus(403);
            response.sendHtml("FORBIDDEN");
            return;
        }

        DataBase.deleteSchedule(scheduleId);

        response.sendRedirect("/schedule/list");
    }

    private boolean isLoggedIn(HttpRequest request) { return getLoginUser(request) != null; }

    private User getLoginUser(HttpRequest request) {
        String sessionId = request.getCookie("sid");
        if (sessionId == null) return null;
        return (User) SessionManager.getSession(sessionId);
    }

}
