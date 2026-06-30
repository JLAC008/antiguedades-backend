package com.antiguedades.auth;

import com.antiguedades.auth.dto.LoginRequest;
import com.antiguedades.exception.BusinessException;
import com.antiguedades.security.JwtTokenProvider;
import com.antiguedades.user.AppUser;
import com.antiguedades.user.UserRepository;
import com.antiguedades.user.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Test
    void rejectsLegacyPlainTextPassword() {
        UserRepository userRepository = mock(UserRepository.class);
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        AppUser user = new AppUser("admin@example.com", "admin123", UserRole.admin, "Admin");
        when(userRepository.findByEmailIgnoreCase("admin@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("admin123", "admin123")).thenReturn(false);
        AuthService authService = new AuthService(userRepository, jwtTokenProvider, passwordEncoder);

        assertThatThrownBy(() -> authService.login(new LoginRequest("admin@example.com", "admin123")))
            .isInstanceOf(BusinessException.class);

        verify(userRepository, never()).save(any());
        verifyNoInteractions(jwtTokenProvider);
    }
}
