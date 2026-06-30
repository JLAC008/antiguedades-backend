package com.antiguedades.auth;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoginAttemptServiceTest {

    @Test
    void blocksAccountAfterConfiguredNumberOfFailures() {
        LoginAttemptService service = new LoginAttemptService(3, Duration.ofMinutes(15), Clock.systemUTC());
        String key = "account:127.0.0.1|user@example.com";

        service.recordFailure(key);
        service.recordFailure(key);
        service.recordFailure(key);

        assertThatThrownBy(() -> service.checkAllowed(key))
            .isInstanceOf(TooManyLoginAttemptsException.class);
    }

    @Test
    void successfulLoginClearsAccountFailures() {
        LoginAttemptService service = new LoginAttemptService(1, Duration.ofMinutes(15), Clock.systemUTC());
        String key = "account:127.0.0.1|user@example.com";
        service.recordFailure(key);

        service.recordSuccess(key);

        assertThatCode(() -> service.checkAllowed(key)).doesNotThrowAnyException();
    }
}
