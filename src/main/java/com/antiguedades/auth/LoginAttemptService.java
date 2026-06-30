package com.antiguedades.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {
    private static final int MAX_TRACKED_KEYS = 10_000;
    private final ConcurrentHashMap<String, AttemptState> attempts = new ConcurrentHashMap<>();
    private final int maxAttempts;
    private final Duration window;
    private final Clock clock;

    @Autowired
    public LoginAttemptService(
        @Value("${app.auth.login-max-attempts:5}") int maxAttempts,
        @Value("${app.auth.login-window-minutes:15}") long windowMinutes) {
        this(maxAttempts, Duration.ofMinutes(windowMinutes), Clock.systemUTC());
    }

    LoginAttemptService(int maxAttempts, Duration window, Clock clock) {
        this.maxAttempts = maxAttempts;
        this.window = window;
        this.clock = clock;
    }

    public void checkAllowed(String key) {
        AttemptState state = attempts.get(key);
        if (state == null) {
            return;
        }
        if (state.expiresAt().isBefore(clock.instant())) {
            attempts.remove(key, state);
            return;
        }
        if (state.failures() >= limitFor(key)) {
            throw new TooManyLoginAttemptsException();
        }
    }

    public void recordFailure(String key) {
        Instant now = clock.instant();
        if (attempts.size() >= MAX_TRACKED_KEYS && !attempts.containsKey(key)) {
            throw new TooManyLoginAttemptsException();
        }
        attempts.compute(key, (ignored, current) -> {
            if (current == null || current.expiresAt().isBefore(now)) {
                return new AttemptState(1, now.plus(window));
            }
            return new AttemptState(current.failures() + 1, current.expiresAt());
        });
    }

    public void recordSuccess(String key) {
        attempts.remove(key);
    }

    private int limitFor(String key) {
        return key.startsWith("ip:") ? maxAttempts * 4 : maxAttempts;
    }

    private record AttemptState(int failures, Instant expiresAt) {
    }
}
