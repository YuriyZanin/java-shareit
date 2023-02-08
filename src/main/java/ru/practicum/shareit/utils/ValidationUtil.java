package ru.practicum.shareit.utils;

import lombok.experimental.UtilityClass;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.practicum.shareit.utils.exception.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ValidationUtil {
    public static String buildErrorString(FieldError error) {
        return String.format("Поле %s содержит ошибку: \"%s\"", error.getField(), error.getDefaultMessage());
    }

    public static String buildErrorMessage(List<FieldError> errors) {
        return errors.stream()
                .map(ValidationUtil::buildErrorString)
                .collect(Collectors.joining("\n"));
    }

    public static void checkErrors(BindingResult errors) {
        if (errors.hasErrors()) {
            String message = ValidationUtil.buildErrorMessage(errors.getFieldErrors());
            throw new ValidationException(message);
        }
    }
}
