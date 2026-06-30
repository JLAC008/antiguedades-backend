package com.antiguedades.auth;

import com.antiguedades.exception.BusinessException;

public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException() {
        super("Correo o contraseña incorrectos");
    }
}
