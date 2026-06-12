package com.antiguedades.auth;

import com.antiguedades.auth.dto.LoginRequest;
import com.antiguedades.auth.dto.LoginResponse;
import com.antiguedades.exception.BusinessException;
import com.antiguedades.security.JwtTokenProvider;
import com.antiguedades.user.AppUser;
import com.antiguedades.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {
        AppUser user = userRepository.findByEmailIgnoreCase(request.email())
            .orElseThrow(() -> new BusinessException("Correo o contraseña incorrectos"));

        String storedPassword = user.getPassword();
        boolean matches = storedPassword.startsWith("$2a$")
            ? passwordEncoder.matches(request.password(), storedPassword)
            : storedPassword.equals(request.password());

        if (!matches) {
            throw new BusinessException("Correo o contraseña incorrectos");
        }

        if (!storedPassword.startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(request.password()));
            userRepository.save(user);
        }

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole().name(), user.getId().toString());
        return new LoginResponse(token, user.getEmail(), user.getName(), user.getRole().name(), user.getId().toString());
    }
}
