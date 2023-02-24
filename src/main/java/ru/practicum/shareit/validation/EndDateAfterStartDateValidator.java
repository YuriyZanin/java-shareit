package ru.practicum.shareit.validation;

import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EndDateAfterStartDateValidator implements ConstraintValidator<EndDateAfterStartDate, BookingDto> {

    @Override
    public boolean isValid(BookingDto value, ConstraintValidatorContext context) {
        return value.getEnd().isAfter(value.getStart());
    }
}
