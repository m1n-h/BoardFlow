package com.github.m1n_h.BoardFlow.core;

import com.github.m1n_h.BoardFlow.http.HttpRequest;
import com.github.m1n_h.BoardFlow.http.HttpResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpRequestHandler implements Runnable {

    private final Socket socket;

    public HttpRequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            handleClient(socket);
        } catch (Exception e) {
            System.out.println("[Error] 클라이언트 요청 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Current Thread: " + Thread.currentThread().getName());
    }

    private static void handleClient(Socket socket) throws Exception {
        // 1. 클라이언트가 보낸 HTTP Request Header 읽기
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
        );

        OutputStream out = socket.getOutputStream();

        HttpRequest request = new HttpRequest(reader);
        HttpResponse response = new HttpResponse(out, request);

        Router.route(request, response);
    }
}
