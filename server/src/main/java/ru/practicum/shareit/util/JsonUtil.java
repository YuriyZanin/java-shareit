package ru.practicum.shareit.util;

import java.time.format.DateTimeFormatter;

public class JsonUtil {
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String COMMENT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
    public static final DateTimeFormatter COMMENT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(COMMENT_DATE_TIME_FORMAT);

}
