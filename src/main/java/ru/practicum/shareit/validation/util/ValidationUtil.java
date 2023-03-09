package ru.practicum.shareit.validation.util;

import lombok.experimental.UtilityClass;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.practicum.shareit.exception.ValidationException;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ValidationUtil {

    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);

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
