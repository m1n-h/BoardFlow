package com.github.m1n_h.BoardFlow.http;

import com.github.m1n_h.BoardFlow.model.User;
import com.github.m1n_h.BoardFlow.util.FileUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class TemplateEngine {

    public static String render(String html, Map<String, String> model) throws IOException {
        if (html == null || model == null) return html;

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

        if (model != null) {
            for (Map.Entry<String, String> entry : model.entrySet()) {
                String placeholder = "{{" + entry.getKey() + "}}";
                result = result.replace(placeholder, entry.getValue() != null ? entry.getValue() : "");
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
}
