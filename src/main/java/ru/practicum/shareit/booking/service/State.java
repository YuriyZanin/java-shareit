package ru.practicum.shareit.booking.service;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static State parseString(String state) {
        try {
            return State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
    }
}
