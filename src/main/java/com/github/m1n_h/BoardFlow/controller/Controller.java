package com.github.m1n_h.BoardFlow.controller;

import com.github.m1n_h.BoardFlow.http.HttpRequest;
import com.github.m1n_h.BoardFlow.http.HttpResponse;

import java.io.IOException;

public interface Controller {
    void process(HttpRequest request, HttpResponse response) throws IOException;
}
