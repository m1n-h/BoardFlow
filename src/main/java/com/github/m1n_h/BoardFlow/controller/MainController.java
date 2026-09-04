package com.github.m1n_h.BoardFlow.controller;

import com.github.m1n_h.BoardFlow.db.DataBase;
import com.github.m1n_h.BoardFlow.http.HttpRequest;
import com.github.m1n_h.BoardFlow.http.HttpResponse;
import com.github.m1n_h.BoardFlow.http.SessionManager;
import com.github.m1n_h.BoardFlow.http.TemplateEngine;
import com.github.m1n_h.BoardFlow.model.User;
import com.github.m1n_h.BoardFlow.util.FileUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainController implements Controller {

    @Override
    public void process(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();

        if ("/".equals(path) || "/index.html".equals(path)) {
            Map<String, Object> model = new HashMap<>();

            String sessionId = request.getCookie("sid");
            if (sessionId != null) {
                User user = (User) SessionManager.getSession(sessionId);
                String adminNav = (user != null && "admin".equals(user.getUserId())) ?
                        "<button type=\"button\" id=\"userListBtn\" class=\"btn btn-sm btn-outline-warning ms-2\">회원목록</button>"
                        : "";

                if (user != null) {
                    model.put("userName", user.getUserName());
                    model.put("adminNav", adminNav);
                }
            }

            response.render("static/index.html", model);
        }
    }
}
