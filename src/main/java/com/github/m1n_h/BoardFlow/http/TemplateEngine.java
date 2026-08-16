package com.github.m1n_h.BoardFlow.http;

import com.github.m1n_h.BoardFlow.model.User;

import java.util.Collection;
import java.util.Map;

public class TemplateEngine {

    public static String render(String html, Map<String, String> model) {
        if (html == null || model == null) return html;

        String result = html;
        for (Map.Entry<String, String> entry : model.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            result = result.replace(placeholder, entry.getValue() != null ? entry.getValue() : "");
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
