package ru.nsu.timetable.exceptions;

public class GenerationInProgressException extends RuntimeException {
    public GenerationInProgressException(String message) {
        super(message);
    }
}
