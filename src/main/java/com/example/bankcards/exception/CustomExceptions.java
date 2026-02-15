package com.example.bankcards.exception;

public class CustomExceptions {
    public static class EntityNotFoundException extends RuntimeException {
        public EntityNotFoundException(String message) {
            super(message);
        }
    }

    public static class InvalidOperationException extends RuntimeException {
        public InvalidOperationException(String message) {
            super(message);
        }
    }

    public static class CardDontBelongUserException extends RuntimeException {
        public CardDontBelongUserException(String message) {
            super(message);
        }
    }
}
