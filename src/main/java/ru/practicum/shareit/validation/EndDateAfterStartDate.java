package ru.practicum.shareit.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EndDateAfterStartDateValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EndDateAfterStartDate {
    String message() default "Дата окончания бронирования добжна быть после даты начала бронирования";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
