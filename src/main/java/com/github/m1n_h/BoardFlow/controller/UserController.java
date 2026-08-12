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
            System.out.println("[Login Success] 사용자 로그인 요청 처리");
            response.sendRedirect("/index.html");
        }
    }
}
