package com.github.m1n_h.BoardFlow.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter DATETIME_LOCAL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter KOREAN_DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm");

    private DateUtils() {}

    public static String toDateTimeLocal(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(DATETIME_LOCAL_FORMATTER);
    }

    public static String toDisplayString(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(DISPLAY_FORMATTER);
    }

    public static String toKoreanDisplayString(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(KOREAN_DISPLAY_FORMATTER);
    }

    public static LocalDateTime parseDateTimeLocal(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) return null;
        return LocalDateTime.parse(dateTimeStr, DATETIME_LOCAL_FORMATTER);
    }

}
