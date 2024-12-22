package ru.nsu.timetable.exceptions;

public class XmlGenerationException extends RuntimeException {
    public XmlGenerationException(String message) {
        super(message);
    }

    public XmlGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
