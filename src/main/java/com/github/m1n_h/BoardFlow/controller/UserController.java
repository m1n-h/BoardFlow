package com.github.m1n_h.BoardFlow.controller;

import com.github.m1n_h.BoardFlow.db.DataBase;
import com.github.m1n_h.BoardFlow.http.HttpRequest;
import com.github.m1n_h.BoardFlow.http.HttpResponse;
import com.github.m1n_h.BoardFlow.http.SessionManager;
import com.github.m1n_h.BoardFlow.model.User;

import java.io.IOException;

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

}
