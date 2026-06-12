package com.antiguedades.user.dto;

import com.antiguedades.user.UserRole;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(UUID id, String email, UserRole role, String name, LocalDateTime createdAt) {
}
