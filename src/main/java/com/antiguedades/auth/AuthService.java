package com.antiguedades.auth;

import com.antiguedades.auth.dto.LoginRequest;
import com.antiguedades.auth.dto.LoginResponse;
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

    public LoginResult login(LoginRequest request) {
        AppUser user = userRepository.findByEmailIgnoreCase(request.email())
            .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole().name(), user.getId().toString());
        LoginResponse response = new LoginResponse(
            user.getEmail(), user.getName(), user.getRole().name(), user.getId().toString());
        return new LoginResult(token, response);
    }
}
