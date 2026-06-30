package com.antiguedades.auth;

public class TooManyLoginAttemptsException extends RuntimeException {
    public TooManyLoginAttemptsException() {
        super("Demasiados intentos de inicio de sesión. Inténtalo de nuevo más tarde.");
    }
}
