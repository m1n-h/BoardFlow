package com.github.m1n_h.BoardFlow.core;

import com.github.m1n_h.BoardFlow.controller.*;
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
        BoardController boardController = new BoardController();
        ScheduleController scheduleController = new ScheduleController();

        mappings.put("/", mainController);
        mappings.put("/index.html", mainController);

        mappings.put("/login", userController);
        mappings.put("/login.html", userController);
        mappings.put("/user/login", userController);
        mappings.put("/user/login.html", userController);

        mappings.put("/join", userController);
        mappings.put("/logout", userController);

        mappings.put("/loginNav", userController);
        mappings.put("/common/loginNav.html", userController);
        mappings.put("/logoutNav", userController);
        mappings.put("/common/logoutNav.html", userController);

        mappings.put("/user/list", userController);
        mappings.put("/user/list.html", userController);

        mappings.put("/user/mypage", userController);
        mappings.put("/user/mypage.html", userController);
        mappings.put("/mypage", userController);
        mappings.put("/mypage.html", userController);

        mappings.put("/board", boardController);
        mappings.put("/board/list", boardController);
        mappings.put("/board/list.html", boardController);
        mappings.put("/board/modify", boardController);
        mappings.put("/board/modify.html", boardController);
        mappings.put("/board/write", boardController);
        mappings.put("/board/write.html", boardController);
        mappings.put("/board/detail", boardController);
        mappings.put("/board/detail.html", boardController);
        mappings.put("/board/delete", boardController);

        mappings.put("/schedule", scheduleController);
        mappings.put("/schedule/list", scheduleController);
        mappings.put("/schedule/list.html", scheduleController);
        mappings.put("/schedule/modify", scheduleController);
        mappings.put("/schedule/modify.html", scheduleController);
        mappings.put("/schedule/write", scheduleController);
        mappings.put("/schedule/write.html", scheduleController);
        mappings.put("/schedule/detail", scheduleController);
        mappings.put("/schedule/detail.html", scheduleController);
        mappings.put("/schedule/delete", scheduleController);
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
