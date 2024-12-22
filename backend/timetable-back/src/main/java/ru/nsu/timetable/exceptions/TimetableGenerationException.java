package ru.nsu.timetable.exceptions;

public class TimetableGenerationException extends RuntimeException {
    public TimetableGenerationException(String message) {
        super(message);
    }

    public TimetableGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
