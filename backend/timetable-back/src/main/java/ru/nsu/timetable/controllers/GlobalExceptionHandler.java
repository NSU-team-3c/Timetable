package ru.nsu.timetable.controllers;

import jakarta.security.auth.message.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.nsu.timetable.exceptions.*;
import ru.nsu.timetable.payload.response.MessageResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<MessageResponse> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(ParsingException.class)
    public ResponseEntity<MessageResponse> handleParsingException(ParsingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse("Error parsing timetable: " + ex.getMessage()));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<MessageResponse> handleAuthException(AuthException ex) {
        return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleGlobalException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error"));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<MessageResponse> handleInvalidDataException(InvalidDataException ex) {
        return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<MessageResponse> handleInvalidTokenException(InvalidTokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new MessageResponse("Invalid Token: " + ex.getMessage()));
    }

    @ExceptionHandler(TimetableGenerationException.class)
    public ResponseEntity<MessageResponse> handleTimetableGenerationException(TimetableGenerationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("Timetable generation error: " + ex.getMessage()));
    }

    @ExceptionHandler(TimetableParsingException.class)
    public ResponseEntity<MessageResponse> handleTimetableParsingException(TimetableParsingException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("Timetable parsing error: " + ex.getMessage()));
    }

    @ExceptionHandler(PrologExecutionException.class)
    public ResponseEntity<MessageResponse> handlePrologExecutionException(PrologExecutionException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("Prolog execution failed: " + ex.getMessage()));
    }

    @ExceptionHandler(XmlGenerationException.class)
    public ResponseEntity<MessageResponse> handleXmlGenerationException(XmlGenerationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("XML generation failed: " + ex.getMessage()));
    }

    @ExceptionHandler(GenerationInProgressException.class)
    public ResponseEntity<MessageResponse> handleGenerationInProgressException(GenerationInProgressException ex) {
        return ResponseEntity.status(HttpStatus.LOCKED)
                .body(new MessageResponse("Generation in progress: " + ex.getMessage()));
    }
}
