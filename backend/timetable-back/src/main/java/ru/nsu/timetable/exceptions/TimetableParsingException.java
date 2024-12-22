package ru.nsu.timetable.exceptions;

public class TimetableParsingException extends RuntimeException {
    public TimetableParsingException(String message) {
        super(message);
    }

    public TimetableParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
