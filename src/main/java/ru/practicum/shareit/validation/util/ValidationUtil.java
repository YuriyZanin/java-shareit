package ru.practicum.shareit.validation.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.practicum.shareit.exception.ValidationException;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationUtil {

    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String COMMENT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
    public static final DateTimeFormatter COMMENT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(COMMENT_DATE_TIME_FORMAT);

    private static String buildErrorString(FieldError error) {
        return String.format("Поле %s содержит ошибку: \"%s\"", error.getField(), error.getDefaultMessage());
    }

    private static String buildErrorMessage(List<FieldError> errors) {
        return errors.stream()
                .map(ValidationUtil::buildErrorString)
                .collect(Collectors.joining("\n "));
    }

    public static void checkErrors(BindingResult errors) {
        if (errors.hasErrors()) {
            String message = ValidationUtil.buildErrorMessage(errors.getFieldErrors());
            throw new ValidationException(message);
        }
    }
}
