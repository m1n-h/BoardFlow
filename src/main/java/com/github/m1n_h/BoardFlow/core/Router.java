package com.github.m1n_h.BoardFlow.core;

import com.github.m1n_h.BoardFlow.controller.Controller;
import com.github.m1n_h.BoardFlow.controller.UserController;
import com.github.m1n_h.BoardFlow.http.HttpRequest;
import com.github.m1n_h.BoardFlow.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class Router {

    private static final Map<String, Controller> mappings = new HashMap<>();

    static {
        mappings.put("/login", new UserController());
        //mappings.put("/board", new BoardController());
        //mappings.put("/schedule", new ScheduleController());
    }

    public static void route(HttpRequest request, HttpResponse response) {
        String path = request.getPath();

        if (mappings.containsKey(path)) {
            Controller controller = mappings.get(path);
            controller.process(request, response);
            return;
        }

        response.forward(path);
    }
}
