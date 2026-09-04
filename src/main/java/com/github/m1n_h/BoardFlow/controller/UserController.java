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
import java.util.HashMap;
import java.util.Map;

public class UserController implements Controller {

    @Override
    public void process(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();
        String method = request.getMethod();

        if (("POST".equalsIgnoreCase(method) || "GET".equalsIgnoreCase(method)) &&
                ("/login".equals(path) || "/user/login".equals(path)
                || "login.html".equals(path) || "/user/login.html".equals(path))
        ) {
            handleLogin(request, response);
        } else if ("/logout".equals(path) || "/user/logout".equals(path)) {
            handleLogout(request, response);
        } else if ("POST".equalsIgnoreCase(method) && ("/join".equals(path) || "/user/join".equals(path))) {
            handleJoin(request, response);
        } else if ("GET".equalsIgnoreCase(method) &&
                ("/mypage".equals(path) || "/user/mypage".equals(path)
                || "/mypage.html".equals(path) || "/user/mypage.html".equals(path))
        ) {
            handleMyPage(request, response);
        } else if ("GET".equalsIgnoreCase(method) &&
                ("/list".equals(path) || "/user/list".equals(path)
                || "/list.html".equals(path) || "/user/list.html".equals(path))
        ) {
            handleUserList(request, response);
        }
    }

    private void handleLogin(HttpRequest request, HttpResponse response) throws IOException {
        String userId = request.getParam("user-id");
        String userPw = request.getParam("user-pw");

        User user = DataBase.findUserById(userId);

        if (user != null && user.getUserPw().equals(userPw)) {
            String sessionId = SessionManager.createSession(user);
            response.setCookie("sid", sessionId, "/");
            response.sendHtml("SUCCESS");
        } else {
            response.sendHtml("FAIL");
        }
    }

    private void handleLogout(HttpRequest request, HttpResponse response) throws IOException {
        String sessionId = request.getCookie("sid");

        if (sessionId != null) {
            SessionManager.removeSession(sessionId);
            response.deleteCookie("sid", "/");
        }

        response.sendRedirect("/");
    }

    private void handleUserList(HttpRequest request, HttpResponse response) throws IOException {
        String sessionId = request.getCookie("sid");
        User loginUser = (User) SessionManager.getSession(sessionId);

        if (loginUser == null) {
            response.setStatus(401);
            response.sendHtml("UNAUTHORIZED");
            return;
        }

        if (!"admin".equals(loginUser.getUserId())) {
            response.setStatus(403);
            response.sendHtml("FORBIDDEN");
            return;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("users", DataBase.findAll());

        response.render("static/user/list.html", model);
    }

    private void handleJoin(HttpRequest request, HttpResponse response) throws IOException {
        String userId = request.getParam("user-id");
        String userPw = request.getParam("user-pw");
        String userName = request.getParam("user-name");

        String email = request.getParam("user-email");
        String domain = request.getParam("user-domain");

        if (domain == null || domain.trim().isEmpty() || "etc".equals(domain)) domain = request.getParam("etc-domain");
        if (domain == null) domain = "";

        String userEmail = (email != null ? email : "") + "@" + domain;

        User user = new User(userId, userPw, userName, userEmail);
        DataBase.addUser(user);

//        response.sendRedirect("/");
        response.sendHtml("SUCCESS");
    }

    private void handleMyPage(HttpRequest request, HttpResponse response) throws IOException {
        String sessionId = request.getCookie("sid");
        if (sessionId == null || !SessionManager.isValidSession(sessionId)) {
            response.sendRedirect("/");
            return;
        }

        User currentUser = (User) SessionManager.getSession(sessionId);
        if (currentUser == null) {
            response.sendRedirect("/");
            return;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("userName", currentUser.getUserName());
        model.put("userId", currentUser.getUserId());
        model.put("userEmail", currentUser.getUserEmail());

        response.render("static/user/mypage.html", model);
    }

}
