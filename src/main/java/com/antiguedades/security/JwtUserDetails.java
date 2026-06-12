package com.antiguedades.security;

public record JwtUserDetails(String username, String role, String userId) {
}
