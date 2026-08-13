package com.github.m1n_h.BoardFlow.controller;

import com.github.m1n_h.BoardFlow.http.HttpRequest;
import com.github.m1n_h.BoardFlow.http.HttpResponse;

public class UserController implements Controller {

    @Override
    public void process(HttpRequest request, HttpResponse response) {
        String method = request.getMethod();
        if ("GET".equalsIgnoreCase(method)) {
            response.forward("/login.html");
        } else if ("POST".equalsIgnoreCase(method)) {
            handleLogin(request, response);
        }
    }

    private void handleLogin(HttpRequest request, HttpResponse response) {
        String businessId = "admin";
        String businessPw = "123456";

        String userId = request.getParam("user-id");
        String userPw = request.getParam("user-pw");

        if (businessId.equals(userId) && businessPw.equals(userPw)) {
            System.out.println("로그인 성공!");
            response.sendRedirect("/index.html");
        } else {
            System.out.println("[로그인 실패] 아이디 및 비밀번호를 다시 입력해주세요.");
            response.sendRedirect("/login");
        }
    }
}
