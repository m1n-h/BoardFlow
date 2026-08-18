package com.github.m1n_h.BoardFlow.controller;

import com.github.m1n_h.BoardFlow.db.DataBase;
import com.github.m1n_h.BoardFlow.http.HttpRequest;
import com.github.m1n_h.BoardFlow.http.HttpResponse;
import com.github.m1n_h.BoardFlow.http.SessionManager;
import com.github.m1n_h.BoardFlow.http.TemplateEngine;
import com.github.m1n_h.BoardFlow.model.User;
import com.github.m1n_h.BoardFlow.util.FileUtil;

import java.io.IOException;
import java.util.Collection;

public class UserController implements Controller {

    @Override
    public void process(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();
        String method = request.getMethod();

        if ("GET".equalsIgnoreCase(method)) {
            response.forward("/login.html");
        } else if ("POST".equalsIgnoreCase(method) && "/login".equals(path)) {
            handleLogin(request, response);
        } else if ("/logout".equals(path)) {
            handleLogout(request, response);
        }
    }

    private void handleLogin(HttpRequest request, HttpResponse response) throws IOException {
        String userId = request.getParam("user-id");
        String userPw = request.getParam("user-pw");

        User user = DataBase.findUserById(userId);

        if (user != null && user.getUserPw().equals(userPw)) {
            String sessionId = SessionManager.createSession(user);
            response.setCookie("sid", sessionId, "/");
            response.sendRedirect("/index.html");
        } else {
            response.sendRedirect("/user/login_failed.html");
        }
    }

    private void handleLogout(HttpRequest request, HttpResponse response) throws IOException {
        String sessionId = request.getCookie("sid");

        if (sessionId != null) {
            SessionManager.removeSession(sessionId);
            response.deleteCookie("sid", "/");
        }

        response.sendRedirect("/index.html");
    }

    private void handleUserList(HttpRequest request, HttpResponse response) throws IOException {
        String sessionId = request.getCookie("sid");
        User loginUser = (User) SessionManager.getSession(sessionId);

        if (loginUser == null) {
            response.sendRedirect("/user/login.html");
            return;
        }

        String html = FileUtil.readFileAsString("static/user/list.html");

        Collection<User> users = DataBase.findAll();
        String renderHtml = TemplateEngine.renderUserList(html, users);

        response.sendHtml(renderHtml);
    }

}
