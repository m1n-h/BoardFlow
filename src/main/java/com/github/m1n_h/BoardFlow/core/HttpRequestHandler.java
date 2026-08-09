package com.github.m1n_h.BoardFlow.core;

import com.github.m1n_h.BoardFlow.http.HttpRequest;

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
        try (Socket socket = this.socket) {
            handleClient(socket);
        } catch (Exception e) {
            System.err.println("[Error] 클라이언트 요청 중 오류 발생: " + e.getMessage());
        }
        System.out.println("Current Thread: " + Thread.currentThread().getName());
    }

    private static void handleClient(Socket socket) throws Exception {
        // 1. 클라이언트가 보낸 HTTP Request Header 읽기
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
        );

        HttpRequest request = new HttpRequest(reader);
        String path = request.getPath();

        if (path == null || path.equals("/")) path = "/index.html";

        String resourcesPath = "static" + path;

        OutputStream output = socket.getOutputStream();

        try (InputStream in = HttpRequestHandler.class.getClassLoader().getResourceAsStream(resourcesPath)) {
            if (in == null) {
                String errorMsg = "<h1>404 Not Found</h1><p>요청하신 파일을 찾을 수 없습니다: " + path + "</p>";
                byte[] errorBytes = errorMsg.getBytes(StandardCharsets.UTF_8);

                output.write("HTTP/1.1 404 Not Found\r\n".getBytes(StandardCharsets.UTF_8));
                output.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes(StandardCharsets.UTF_8));
                output.write(("Content-Length: " + errorBytes.length + "\r\n").getBytes(StandardCharsets.UTF_8));
                output.write("Connection: close\r\n".getBytes(StandardCharsets.UTF_8));
                output.write("\r\n".getBytes(StandardCharsets.UTF_8));
                output.write(errorBytes);
                output.flush();
                return;
            }

            byte[] bodyBytes = in.readAllBytes();

            output.write("HTTP/1.1 200 OK\r\n".getBytes(StandardCharsets.UTF_8));
            output.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes(StandardCharsets.UTF_8));
            output.write(("Content-Length: " + bodyBytes.length + "\r\n").getBytes(StandardCharsets.UTF_8));
            output.write("Connection: close\r\n".getBytes(StandardCharsets.UTF_8));
            output.write("\r\n".getBytes(StandardCharsets.UTF_8));
            output.write(bodyBytes);
            output.flush();
        }

    }
}
