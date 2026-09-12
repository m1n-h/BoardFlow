package com.github.m1n_h.BoardFlow.http;

import com.github.m1n_h.BoardFlow.model.User;
import com.github.m1n_h.BoardFlow.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public class TemplateEngine {

    public static String render(String html, Map<String, Object> model, HttpRequest request) throws IOException {
        if (html == null) return "";

        String result = html;

        result = includeComponents(result, request);

        if (model != null) {
            for (Map.Entry<String, Object> entry : model.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value instanceof Collection<?>) {
                    result = renderListBlock(result, key, (Collection<?>) value);
                } else {
                    String strValue = (value != null) ? value.toString() : "";
                    result = result.replace("{{" + key + "}}", strValue);
                }
            }
        }

        return result;
    }

    private static String renderListBlock(String html, String key, Collection<?> list) {
        String startTag = "{{#" + key +  "}}";
        String endTag = "{{/" + key + "}}";

        int startIndex = html.indexOf(startTag);
        int endIndex = html.indexOf(endTag);

        if (startIndex == -1 || endIndex == -1) return html;

        String templateBlock = html.substring(startIndex + startTag.length(), endIndex);
        StringBuilder sb = new StringBuilder();

        int index = 1;
        for (Object item : list) {
            String row = templateBlock.replace("{{index}}", String.valueOf(index++));

            Field[] fields = item.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object fieldValue = field.get(item);
                    String fieldStr =  (fieldValue != null) ? fieldValue.toString() : "";
                    row = row.replace("{{" + field.getName() + "}}", fieldStr);
                } catch (IllegalAccessException e) {}
            }
            sb.append(row);
        }

        return html.substring(0, startIndex) + sb.toString() + html.substring(endIndex + endTag.length());
    }

    private static String includeComponents(String html, HttpRequest request) throws IOException {
        if (html == null) return "";

        String header = FileUtil.readFileAsString("static/common/header.html");
        String footer = FileUtil.readFileAsString("static/common/footer.html");
        String modal = FileUtil.readFileAsString("static/common/modal.html");

        String sessionId = (request != null) ? request.getCookie("sid") : null;
        User loginUser = (sessionId != null) ? (User) SessionManager.getSession(sessionId) : null;

        String navPath = (loginUser != null) ? "static/common/loginNav.html" : "static/common/logoutNav.html";
        String nav = FileUtil.readFileAsString(navPath);

        if (nav != null) {
            if (loginUser != null && "admin".equals(loginUser.getUserId())) {
                String adminBtn = "<button type=\"button\" id=\"userListBtn\" class=\"btn btn-sm btn-outline-secondary ms-2\">회원목록</button>";
                nav = nav.replace("{{adminNav}}", adminBtn);
            } else {
                nav = nav.replace("{{adminNav}}", "");
            }
        }

        String result = html;
        if (header != null) result = result.replace("{{header}}", header);
        if (footer != null) result = result.replace("{{footer}}", footer);
        if (modal != null) result = result.replace("{{modal}}", modal);
        if (nav != null) result = result.replace("{{loginNav}}", nav);

        return result;
    }

}
