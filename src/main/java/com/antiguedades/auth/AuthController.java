package com.antiguedades.auth;

import com.antiguedades.auth.dto.LoginRequest;
import com.antiguedades.auth.dto.LoginResponse;
import com.antiguedades.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Locale;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final LoginAttemptService loginAttemptService;
    private final String cookieName;
    private final boolean cookieSecure;
    private final Duration cookieLifetime;

    public AuthController(
        AuthService authService,
        LoginAttemptService loginAttemptService,
        @Value("${app.auth.cookie-name:AUTH_TOKEN}") String cookieName,
        @Value("${app.auth.cookie-secure:false}") boolean cookieSecure,
        @Value("${app.jwt.expiration-ms}") long expirationMs) {
        this.authService = authService;
        this.loginAttemptService = loginAttemptService;
        this.cookieName = cookieName;
        this.cookieSecure = cookieSecure;
        this.cookieLifetime = Duration.ofMillis(expirationMs);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletRequest servletRequest) {
        String clientIp = clientIp(servletRequest);
        String accountKey = "account:" + clientIp + "|" + request.email().trim().toLowerCase(Locale.ROOT);
        String ipKey = "ip:" + clientIp;
        loginAttemptService.checkAllowed(accountKey);
        loginAttemptService.checkAllowed(ipKey);
        try {
            LoginResult result = authService.login(request);
            loginAttemptService.recordSuccess(accountKey);
            return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authCookie(result.token(), cookieLifetime).toString())
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .body(result.user());
        } catch (BusinessException exception) {
            loginAttemptService.recordFailure(accountKey);
            loginAttemptService.recordFailure(ipKey);
            throw exception;
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent()
            .header(HttpHeaders.SET_COOKIE, authCookie("", Duration.ZERO).toString())
            .build();
    }

    private ResponseCookie authCookie(String value, Duration maxAge) {
        return ResponseCookie.from(cookieName, value)
            .httpOnly(true)
            .secure(cookieSecure)
            .sameSite("Strict")
            .path("/")
            .maxAge(maxAge)
            .build();
    }

    private String clientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        return forwardedFor == null || forwardedFor.isBlank()
            ? request.getRemoteAddr()
            : forwardedFor.split(",", 2)[0].trim();
    }
}
