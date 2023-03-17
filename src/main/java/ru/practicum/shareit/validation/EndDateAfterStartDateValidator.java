package ru.practicum.shareit.validation;

import ru.practicum.shareit.booking.dto.BookingCreationDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EndDateAfterStartDateValidator implements ConstraintValidator<EndDateAfterStartDate, BookingCreationDto> {

    @Override
    public boolean isValid(BookingCreationDto value, ConstraintValidatorContext context) {
        return value.getStart() != null && value.getEnd() != null && value.getEnd().isAfter(value.getStart());
    }
}
