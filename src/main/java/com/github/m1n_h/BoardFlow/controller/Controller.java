package com.github.m1n_h.BoardFlow.controller;

import com.github.m1n_h.BoardFlow.http.HttpRequest;
import com.github.m1n_h.BoardFlow.http.HttpResponse;

public interface Controller {
    void process(HttpRequest request, HttpResponse response);
}
