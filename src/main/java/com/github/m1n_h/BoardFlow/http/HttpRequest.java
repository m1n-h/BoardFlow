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
    private final Map<String, String> cookies = new HashMap<>();

    public HttpRequest(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.trim().isEmpty()) return;

        String[] tokens = requestLine.split(" ");
        if (tokens.length >= 2) {
            this.method = tokens[0];
            parseUrlAndQueryString(tokens[1]);
        }

        String line;
        while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
            int index = line.indexOf(":");
            if (index > 0) {
                String headerName = line.substring(0, index).trim().toLowerCase();
                String headerValue = line.substring(index + 1).trim();
                headers.put(headerName, headerValue);
            }
        }

        parseCookies();

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

            String[] keyValue = pair.split("=", 2);
            String rawKey = keyValue[0];
            String rawValue = (keyValue.length > 1) ? keyValue[1] : "";

            String key;
            String value;

            try {
                key = URLDecoder.decode(rawKey, StandardCharsets.UTF_8);
            } catch (Exception e) {
                System.err.println("[Warn] Parameter Key URLDecode Failed (" + rawKey + "): " + e.getMessage());
                key = rawKey;
            }

            try {
                value = URLDecoder.decode(rawValue, StandardCharsets.UTF_8);
            } catch (Exception e) {
                System.err.println("[Warn] Parameter Value URLDecode Failed (" + rawValue + "): " + e.getMessage());
                value = rawValue;
            }

            params.put(key, value);
        }
    }

    private void parseCookies() {
        String cookiesHeader = getHeader("cookie");
        if (cookiesHeader == null || cookiesHeader.isEmpty()) return;

        String[] pairs = cookiesHeader.split(";");
        for (String pair : pairs) {
            String[] keyValue = pair.trim().split("=", 2);
            if (keyValue.length == 2) cookies.put(keyValue[0].trim(), keyValue[1].trim());
        }
    }

    public String getMethod() { return method; }
    public String getPath() { return path; }
    public String getHeader(String name) {
        if (name == null) return null;
        return headers.get(name.toLowerCase());
    }
    public String getParam(String name) { return params.get(name); }
    public String getCookie(String name) { return cookies.get(name); }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", headers=" + headers +
                '}';
    }
}
