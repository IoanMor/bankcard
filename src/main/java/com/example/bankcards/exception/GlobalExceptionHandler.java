package com.example.bankcards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> response(HttpStatus status, String message) {
        Map<String, Object> mapInfo = new HashMap<>();
        mapInfo.put("timestamp", LocalDateTime.now());
        mapInfo.put("status", status.value());
        mapInfo.put("error", status.getReasonPhrase());
        mapInfo.put("message", message);
        return new ResponseEntity<>(mapInfo, status);
    }

    @ExceptionHandler(CustomExceptions.EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(CustomExceptions.EntityNotFoundException ex) {
        return response(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.InvalidOperationException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidOperation(CustomExceptions.InvalidOperationException ex) {
        return response(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CustomExceptions.CardDontBelongUserException.class)
    public ResponseEntity<Map<String,Object>> handleCardDontBelongUser(CustomExceptions.CardDontBelongUserException ex){
        return response(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        return response(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return response(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
