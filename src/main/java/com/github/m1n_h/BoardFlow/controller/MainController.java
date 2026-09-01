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
            response.render("static/index.html", null);
        }
    }
}
