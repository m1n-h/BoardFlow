package com.github.m1n_h.BoardFlow.http;

import com.github.m1n_h.BoardFlow.model.User;
import com.github.m1n_h.BoardFlow.util.FileUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class TemplateEngine {

    public static String render(String html, Map<String, String> model, HttpRequest request) throws IOException {
        if (html == null) return html;

        String result = html;

        if (result.contains("{{header}}")) {
            String headerContent = FileUtil.readFileAsString("static/common/header.html");
            result = result.replace("{{header}}", headerContent);
        }

        if (result.contains("{{footer}}")) {
            String footerContent = FileUtil.readFileAsString("static/common/footer.html");
            result = result.replace("{{footer}}", footerContent);
        }

        if (result.contains("{{modal}}")) {
            String modalContent = FileUtil.readFileAsString("static/common/modal.html");
            result = result.replace("{{modal}}", modalContent);
        }

        if (result.contains("{{loginNav}}")) {
            String sessionId = (request != null) ? request.getCookie("sid") : null;
            User user = (sessionId != null) ? (User) SessionManager.getSession(sessionId) : null;
            result = result.replace("{{loginNav}}", buildLoginNavHtml(user));
        }

        if (model != null) {
            for (Map.Entry<String, String> entry : model.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue() != null ? entry.getValue() : "";
                String placeholder = "{{" + key + "}}";

                result = result.replace(placeholder, value);
            }
        }

        return result;
    }

    public static String renderUserList(String html, Collection<User> users) {
        if (html == null) return html;

        String startTag = "{{#users}}";
        String endTag = "{{/users}}";

        int startIndex = html.indexOf(startTag);
        int endIndex = html.indexOf(endTag);

        if (startIndex == -1 || endIndex == -1) return html;

        String templateBlock = html.substring(startIndex + startTag.length(), endIndex);

        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (User user : users) {
            String now = templateBlock
                    .replace("{{index}}", String.valueOf(index++))
                    .replace("{{userId}}", user.getUserId())
                    .replace("{{userName}}", user.getUserName())
                    .replace("{{userEmail}}", user.getUserEmail());
            sb.append(now);
        }

        return html.substring(0, startIndex) + sb.toString() + html.substring(endIndex + endTag.length());
    }

    private static String buildLoginNavHtml(User user) {
        String loginNavHtml = "";
        if (user != null) {
            loginNavHtml = "<span><strong>" + user.getUserName() + "</strong> 님 환영합니다!</span>"
                    + "<button type='button' id='myPageBtn' class='btn btn-sm btn-outline-secondary ms-2'>마이페이지</button>"
                    + "<button type='button' id='logoutBtn' class='btn btn-sm btn-outline-danger ms-2'>로그아웃</button>";
        } else {
            loginNavHtml = "<button type='button' id='loginBtn' class='btn btn-sm btn-outline-primary'>로그인</button>"
                    + "<button type='button' id='joinBtn' class='btn btn-sm btn-outline-secondary ms-2'>회원가입</button>";
        }

        return loginNavHtml;
    }
}
