package com.github.m1n_h.BoardFlow.core;

import com.github.m1n_h.BoardFlow.controller.Controller;
import com.github.m1n_h.BoardFlow.controller.MainController;
import com.github.m1n_h.BoardFlow.controller.UserController;
import com.github.m1n_h.BoardFlow.http.HttpRequest;
import com.github.m1n_h.BoardFlow.http.HttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Router {

    private static final Map<String, Controller> mappings = new HashMap<>();

    static {
        MainController mainController = new MainController();
        UserController userController = new UserController();

        mappings.put("/", mainController);
        mappings.put("/index.html", mainController);

        mappings.put("/login", userController);
        mappings.put("/join", userController);
        mappings.put("/logout", userController);
        mappings.put("/user/login", userController);
        mappings.put("/user/list", userController);

        mappings.put("/user/mypage", userController);
        mappings.put("/user/mypage.html", userController);
        mappings.put("/mypage", userController);
        mappings.put("/mypage.html", userController);

        //mappings.put("/board", new BoardController());
        //mappings.put("/schedule", new ScheduleController());
    }

    public static void route(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();

        if (mappings.containsKey(path)) {
            Controller controller = mappings.get(path);
            controller.process(request, response);
            return;
        }

        response.forward(path);
    }
}
