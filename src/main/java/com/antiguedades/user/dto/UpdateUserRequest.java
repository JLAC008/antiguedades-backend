package com.antiguedades.user.dto;

import com.antiguedades.user.UserRole;

public record UpdateUserRequest(
    String name,
    String email,
    String password,
    UserRole role
) {
}
