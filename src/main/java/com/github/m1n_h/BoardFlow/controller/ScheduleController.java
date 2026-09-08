package com.github.m1n_h.BoardFlow.controller;

import com.github.m1n_h.BoardFlow.http.HttpRequest;
import com.github.m1n_h.BoardFlow.http.HttpResponse;

import java.io.IOException;

public class ScheduleController implements Controller {

    @Override
    public void process(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();
        String method = request.getMethod();
    }
}
