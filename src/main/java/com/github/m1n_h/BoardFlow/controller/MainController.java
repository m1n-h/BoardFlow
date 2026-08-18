package com.github.m1n_h.BoardFlow.controller;

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
            String html = FileUtil.readFileAsString("static/index.html");

            String sessionId = request.getCookie("sid");
            User user = (User) SessionManager.getSession(sessionId);

            Map<String, String> model = new HashMap<>();
            if (user != null) {
                model.put(
                        "loginNav",
                        "<span>" + user.getUserName() + "님 환영합니다!</span>"
                        + "<button type='button' id='logoutBtn' class='d-none'>로그아웃</button>"
                );
            } else {
                model.put(
                        "loginNav",
                        "<button type='button' id='loginBtn'>로그인</button>"
                        + "<button type='button' id='joinBtn'>회원가입</button>"
                );
            }

            String renderedHtml = TemplateEngine.render(html, model);
            response.sendHtml(renderedHtml);
        }
    }
}
