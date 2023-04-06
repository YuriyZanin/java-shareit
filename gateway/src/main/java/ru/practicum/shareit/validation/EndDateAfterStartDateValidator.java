package ru.practicum.shareit.validation;


import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EndDateAfterStartDateValidator implements ConstraintValidator<EndDateAfterStartDate, BookItemRequestDto> {

    @Override
    public boolean isValid(BookItemRequestDto value, ConstraintValidatorContext context) {
        return value.getStart() != null && value.getEnd() != null && value.getEnd().isAfter(value.getStart());
    }
}
