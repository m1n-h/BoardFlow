package com.github.m1n_h.BoardFlow;

import com.github.m1n_h.BoardFlow.core.HttpRequestHandler;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {

    private final int port;
    private final ExecutorService threadPool;

    public WebServer(int port) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(10);
    }

    public void start() {
        System.out.println("=== Java Custom Web Server Starting on Port " + port + " ===");

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {
                Socket clientSocket = serverSocket.accept();
                HttpRequestHandler handler = new HttpRequestHandler(clientSocket);

                threadPool.execute(handler);
            }

        } catch (Exception e) {
            System.err.println("[Server Error] " + e.getMessage());
        }
    }

}
