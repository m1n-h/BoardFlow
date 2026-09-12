package com.github.m1n_h.BoardFlow.controller;

import com.github.m1n_h.BoardFlow.db.DataBase;
import com.github.m1n_h.BoardFlow.http.HttpRequest;
import com.github.m1n_h.BoardFlow.http.HttpResponse;
import com.github.m1n_h.BoardFlow.http.SessionManager;
import com.github.m1n_h.BoardFlow.model.Article;
import com.github.m1n_h.BoardFlow.model.User;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BoardController implements Controller {

    @Override
    public void process(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();
        String method = request.getMethod();

        try {
            if ("GET".equalsIgnoreCase(method) &&
                    ("/list".equals(path) || "/board/list".equals(path)
                            || "/list.html".equals(path) || "/board/list.html".equals(path))
            ) {
                showList(request, response);
            } else if ("GET".equalsIgnoreCase(method) &&
                    ("/detail".equals(path) || "/board/detail".equals(path)
                            || "/detail.html".equals(path) || "/board/detail.html".equals(path))
            ) {
                showDetail(request, response);
            } else if ("GET".equalsIgnoreCase(method) &&
                    ("/write".equals(path) || "/board/write".equals(path)
                            || "/write.html".equals(path) || "/board/write.html".equals(path))
            ) {
                showForm(request, response);
            } else if ("POST".equalsIgnoreCase(method) &&
                    ("/write".equals(path) || "/board/write".equals(path)
                            || "/write.html".equals(path) || "/board/write.html".equals(path))
            ) {
                createArticle(request, response);
            } else if ("GET".equalsIgnoreCase(method) &&
                    ("/modify".equals(path) || "/board/modify".equals(path)
                            || "/modify.html".equals(path) || "/board/modify.html".equals(path))
            ) {
                showUpdateForm(request, response);
            } else if ("POST".equalsIgnoreCase(method) &&
                    ("/modify".equals(path) || "/board/modify".equals(path)
                            || "/modify.html".equals(path) || "/board/modify.html".equals(path))
            ) {
                updateArticle(request, response);
            } else if ("POST".equalsIgnoreCase(method) &&
                    ("/delete".equals(path) || "/board/delete".equals(path))
            ) {
                deleteArticle(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.sendHtml("INTERNAL SERVER ERROR");
        }
    }

    private void showList(HttpRequest request, HttpResponse response) throws IOException {
        Collection<Article> articles = DataBase.findAllArticles();

        String sessionId = request.getCookie("sid");
        User user = (User) SessionManager.getSession(sessionId);

        Map<String, Object> model = new HashMap<>();
        model.put("article", articles);
        model.put("userName", user.getUserName());

        response.render("static/board/list.html", model);
    }

    private void showDetail(HttpRequest request, HttpResponse response) throws IOException {
        String idParam = request.getParam("id");
        if (idParam == null || idParam.isBlank()) {
            response.setStatus(400);
            response.sendHtml("BAD REQUEST: Missing Article ID");
            return;
        }

        Long articleId;
        try {
            articleId = Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            response.setStatus(400);
            response.sendHtml("BAD REQUEST: Invalid Article ID format");
            return;
        }

        Article article = DataBase.findArticleById(articleId);

        if (article == null) {
            response.setStatus(404);
            response.sendHtml("NOT FOUND");
            return;
        }

        String sessionId = request.getCookie("sid");
        User user = (sessionId != null) ? (User) SessionManager.getSession(sessionId) : null;

        boolean isWriter = user != null && article.getWriter().equals(user.getUserName());
        boolean isAdmin = user != null && "관리자".equals(user.getUserName());

        Map<String, Object> model = new HashMap<>();
        model.put("article", article);
        model.put("id", article.getId());
        model.put("title", article.getTitle());
        model.put("content", article.getContent());
        model.put("writer", article.getWriter());
        model.put("createdAt", article.getCreatedAt());
        model.put("userName", user.getUserName());

        if (isWriter || isAdmin) {
            String actionButtons = String.format(
                    "<a href=\"/board/modify?id=%d\" class=\"btn btn-sm btn-success py-2 mx-2 article-update-btn\" data-id=\"%d\">수정</a> " +
                    "<button type=\"button\" onclick=\"deleteArticle(%d)\" class=\"btn btn-sm btn-outline-danger py-2 article-delete-btn\" data-id=\"%d\">삭제</button>",
                    article.getId(), article.getId(), article.getId(), article.getId()
            );
            model.put("actionButtons", actionButtons);
        } else {
            model.put("actionButtons", "");
        }

        response.render("static/board/detail.html", model);
    }

    private void showForm(HttpRequest request, HttpResponse response) throws IOException {
        if (!isLoggedIn(request)) {
            response.setStatus(401);
            response.sendHtml("UNAUTHORIZED");
            return;
        }

        response.render("static/board/write.html", new HashMap<>());
    }

    private void createArticle(HttpRequest request, HttpResponse response) throws IOException {
        User user = getLoginUser(request);
        if (user == null) {
            response.setStatus(401);
            response.sendHtml("UNAUTHORIZED");
            return;
        }

        String title = request.getParam("title");
        String content = request.getParam("content");

        if (title == null || title.isBlank() || content == null || content.isBlank()) {
            response.setStatus(400);
            response.sendHtml("INVALID REQUEST");
            return;
        }

        DataBase.addArticle(title, content, user.getUserName());
        response.sendRedirect("/board/list");
    }

    private void showUpdateForm(HttpRequest request, HttpResponse response) throws IOException {
        User user = getLoginUser(request);
        if (user == null) {
            response.setStatus(401);
            response.sendHtml("UNAUTHORIZED");
            return;
        }

        Long articleId = Long.parseLong(request.getParam("id"));
        Article article = DataBase.findArticleById(articleId);

        if (article == null) {
            response.setStatus(404);
            response.sendHtml("NOT FOUND");
            return;
        }

        if (!article.getWriter().equals(user.getUserName()) && !"관리자".equals(user.getUserName())) {
            response.setStatus(403);
            response.sendHtml("FORBIDDEN");
            return;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("id", article.getId());
        model.put("title", article.getTitle());
        model.put("content", article.getContent());

        response.render("static/board/modify.html", model);
    }

    private void updateArticle(HttpRequest request, HttpResponse response) throws IOException {
        User user = getLoginUser(request);
        if (user == null) {
            response.setStatus(401);
            response.sendHtml("UNAUTHORIZED");
            return;
        }

        Long articleId = Long.parseLong(request.getParam("id"));
        Article article = DataBase.findArticleById(articleId);

        if (article == null) {
            response.setStatus(404);
            response.sendHtml("NOT FOUND");
            return;
        }

        if (!article.getWriter().equals(user.getUserName()) && !"관리자".equals(user.getUserName())) {
            response.setStatus(403);
            response.sendHtml("FORBIDDEN");
            return;
        }

        String title = request.getParam("title");
        String content = request.getParam("content");

        DataBase.updateArticle(articleId, title, content);

        response.sendRedirect("/board/detail?id=" + article.getId());
    }

    private void deleteArticle(HttpRequest request, HttpResponse response) throws IOException {
        User user = getLoginUser(request);
        if (user == null) {
            response.setStatus(401);
            response.sendHtml("UNAUTHORIZED");
            return;
        }

        Long articleId = Long.parseLong(request.getParam("id"));
        Article article = DataBase.findArticleById(articleId);

        if (article == null) {
            response.setStatus(404);
            response.sendHtml("NOT FOUND");
            return;
        }

        boolean isWriter = article.getWriter().equals(user.getUserName());
        boolean isAdmin = "관리자".equals(user.getUserName());

        if (!isWriter && !isAdmin) {
            response.setStatus(403);
            response.sendHtml("FORBIDDEN");
            return;
        }

        DataBase.deleteArticle(articleId);

        response.sendRedirect("/board/list");
    }

    private boolean isLoggedIn(HttpRequest request) { return getLoginUser(request) != null; }

    private User getLoginUser(HttpRequest request) {
        String sessionId = request.getCookie("sid");
        if (sessionId == null) return null;
        return (User) SessionManager.getSession(sessionId);
    }
}
