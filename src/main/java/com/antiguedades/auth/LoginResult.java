package com.antiguedades.auth;

import com.antiguedades.auth.dto.LoginResponse;

public record LoginResult(String token, LoginResponse user) {
}
