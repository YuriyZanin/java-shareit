package ru.practicum.shareit.utils.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NullOrNotBlankValidator.class)
public @interface NullOrNotBlank {
    String message() default "поле не должно быть пустым";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
