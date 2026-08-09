package com.github.m1n_h.BoardFlow.http;

import java.io.BufferedReader;
import java.io.IOException;
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
        String pathTarget = tokens[1];

        String[] queryString = pathTarget.split("\\?", 2);
        this.path = queryString[0];
        this.query = (queryString.length > 1) ? queryString[1] : "";

        this.version = tokens[2];

        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] headerTokens = line.split(":", 2);
            headers.put(headerTokens[0].trim(), headerTokens[1].trim());
        }
    }

    public String getMethod() { return method; }
    public String getPath() { return path; }
    public String getQuery() { return query; }
    public String getVersion() { return version; }
    public String getHeader(String name) { return headers.get(name.toLowerCase()); }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", headers=" + headers +
                '}';
    }
}
