package com.github.m1n_h.BoardFlow.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String path;
    private String query;
    private String version;
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> params = new HashMap<>();

    public HttpRequest(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isEmpty()) return;

        String[] tokens = requestLine.split(" ");
        this.method = tokens[0];
        parseUrlAndQueryString(tokens[1]);

        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            int index = line.indexOf(":");
            if (index > 0) {
                String headerName = line.substring(0, index).trim().toLowerCase();
                String headerValue = line.substring(index + 1).trim();
                headers.put(headerName, headerValue);
            }
        }

        if ("POST".equalsIgnoreCase(this.method)) parseBody(reader);
    }

    private void parseUrlAndQueryString(String url) {
        int index = url.indexOf("?");
        if (index != -1) {
            this.path = url.substring(0, index);
            String queryString = url.substring(index + 1);
            parseParameters(queryString);
        } else {
            this.path = url;
        }
    }

    private void parseBody(BufferedReader reader) throws IOException {
        String contentLengthHeader = getHeader("Content-Length");
        if (contentLengthHeader == null) return;

        int contentLength;
        try {
            contentLength = Integer.parseInt(contentLengthHeader.trim());
        } catch (NumberFormatException e) {
            return;
        }

        if (contentLength <= 0) return;

        char[] bodyChars = new char[contentLength];
        int totalRead = 0;

        while (totalRead < contentLength) {
            int readCount = reader.read(bodyChars, totalRead, contentLength - totalRead);
            if (readCount == -1) break;
            totalRead += readCount;
        }

        if (totalRead > 0) {
            String body = new String(bodyChars, 0, totalRead);
            parseParameters(body);
        }
    }

    private void parseParameters(String queryString) {
        if (queryString == null || queryString.isEmpty()) return;

        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            if (pair.isEmpty()) continue;

            int keyValue = pair.indexOf("=");
            if (keyValue > 0) {
                String key = URLDecoder.decode(pair.substring(0, keyValue), StandardCharsets.UTF_8);
                String value = (keyValue < pair.length() - 1)
                        ? URLDecoder.decode(pair.substring(keyValue + 1), StandardCharsets.UTF_8)
                        : "";
                params.put(key, value);
            } else if (keyValue == -1) {
                String key = URLDecoder.decode(pair, StandardCharsets.UTF_8);
                params.put(key, "");
            }
        }
    }

    public String getMethod() { return method; }
    public String getPath() { return path; }
    public String getHeader(String name) { return headers.get(name.toLowerCase()); }
    public String getParam(String name) { return params.get(name); }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", headers=" + headers +
                '}';
    }
}
