package com.github.m1n_h.BoardFlow.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private final DataOutputStream dos;
    private final Map<String,String> headers = new HashMap<>();

    public HttpResponse(OutputStream outputStream) {
        this.dos = new DataOutputStream(outputStream);
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void forward(String path) {
        try {
            if (path == null || path.equals("/")) path = "/index.html";

            if (path.startsWith("/")) path = path.substring(1);
            String resourcePath = "static/" + path;

            try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
                if (in == null) {
                    forward404(path);
                    return;
                }

                byte[] body = in.readAllBytes();

                addHeader("Content-Type", "text/html;charset=utf-8");
                addHeader("Content-Length", String.valueOf(body.length));

                sendResponse("200 OK", body);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRedirect(String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");

            processHeaders();

            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void forward404(String path) {
        try {
            String errorMsg = "<h1>404 Not Found</h1><p>요청하신 파일을 찾을 수 없습니다: " + path + "</p>";
            byte[] body = errorMsg.getBytes(StandardCharsets.UTF_8);

            addHeader("Content-Type", "text/html;charset=utf-8");
            addHeader("Content-Length", String.valueOf(body.length));

            sendResponse("404 Not Found", body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(String status, byte[] body) throws IOException {
        dos.writeBytes("HTTP/1.1 " + status + "\r\n");
        processHeaders();
        dos.writeBytes("\r\n"); // Header와 Body를 구분하는 빈 줄
        dos.write(body, 0, body.length);
        dos.flush();
    }

    private void processHeaders() throws IOException {
        for (Map.Entry<String, String> header : headers.entrySet()) {
            dos.writeBytes(header.getKey() + ": " + header.getValue() + "\r\n");
        }
    }
}
