package com.antiguedades.auth.dto;

public record LoginResponse(String email, String username, String role, String userId) {
}
