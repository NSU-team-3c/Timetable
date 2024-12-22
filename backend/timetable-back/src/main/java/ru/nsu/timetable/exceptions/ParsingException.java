package ru.nsu.timetable.exceptions;

public class ParsingException extends RuntimeException {
    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
