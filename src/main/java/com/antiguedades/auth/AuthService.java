package com.antiguedades.auth;

import com.antiguedades.auth.dto.LoginRequest;
import com.antiguedades.auth.dto.LoginResponse;
import com.antiguedades.exception.BusinessException;
import com.antiguedades.security.JwtTokenProvider;
import com.antiguedades.user.AppUser;
import com.antiguedades.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(LoginRequest request) {
        AppUser user = userRepository.findByEmailIgnoreCase(request.email())
            .orElseThrow(() -> new BusinessException("Correo o contraseña incorrectos"));

        if (!user.getPassword().equals(request.password())) {
            throw new BusinessException("Correo o contraseña incorrectos");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole().name(), user.getId().toString());
        return new LoginResponse(token, user.getEmail(), user.getName(), user.getRole().name(), user.getId().toString());
    }
}
