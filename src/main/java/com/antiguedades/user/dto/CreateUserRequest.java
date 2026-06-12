package com.antiguedades.user.dto;

import com.antiguedades.user.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
    @NotBlank String name,
    @NotBlank @Email String email,
    @NotBlank @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres") String password,
    @NotNull UserRole role
) {
}
