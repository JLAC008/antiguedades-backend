package com.antiguedades.auth.dto;

public record LoginResponse(String token, String email, String username, String role, String userId) {
}
