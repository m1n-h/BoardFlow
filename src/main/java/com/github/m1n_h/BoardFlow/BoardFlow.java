package com.github.m1n_h.BoardFlow;

public class BoardFlow {

    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        int port = DEFAULT_PORT;

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("[Error] 유효하지 않은 포트 번호입니다. 기본 포트(" + DEFAULT_PORT + ")를 사용합니다.");
            }
        }

        WebServer server = new WebServer(port);
        server.start();
    }
}
