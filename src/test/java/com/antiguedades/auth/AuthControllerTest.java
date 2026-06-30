package com.antiguedades.auth;

import com.antiguedades.auth.dto.LoginRequest;
import com.antiguedades.auth.dto.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Test
    void loginReturnsJwtOnlyInHardenedCookie() {
        AuthService authService = mock(AuthService.class);
        LoginAttemptService attempts = mock(LoginAttemptService.class);
        LoginResponse user = new LoginResponse(
            "admin@example.com", "Superusuario", "superuser", "id");
        when(authService.login(any())).thenReturn(new LoginResult("signed-token", user));
        AuthController controller = new AuthController(
            authService, attempts, "__Host-AUTH_TOKEN", true, 28_800_000);
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRemoteAddr("127.0.0.1");

        ResponseEntity<LoginResponse> response = controller.login(
            new LoginRequest("admin@example.com", "password"), servletRequest);

        String cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertThat(cookie)
            .contains("__Host-AUTH_TOKEN=signed-token")
            .contains("Path=/")
            .contains("Max-Age=28800")
            .contains("Secure")
            .contains("HttpOnly")
            .contains("SameSite=Strict");
        assertThat(response.getBody()).isEqualTo(user);
    }

    @Test
    void logoutExpiresAuthenticationCookie() {
        AuthController controller = new AuthController(
            mock(AuthService.class), mock(LoginAttemptService.class),
            "__Host-AUTH_TOKEN", true, 28_800_000);

        String cookie = controller.logout().getHeaders().getFirst(HttpHeaders.SET_COOKIE);

        assertThat(cookie).contains("__Host-AUTH_TOKEN=").contains("Max-Age=0");
    }
}
