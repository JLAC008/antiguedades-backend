package com.antiguedades.config;

import com.antiguedades.user.AppUser;
import com.antiguedades.user.UserRepository;
import com.antiguedades.user.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class SuperuserBootstrap implements ApplicationRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String username;
    private final String password;

    public SuperuserBootstrap(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        @Value("${app.superuser.username}") String username,
        @Value("${app.superuser.password}") String password) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.username = requireValue(username, "SUPERUSER_USERNAME").toLowerCase(Locale.ROOT);
        this.password = requireValue(password, "SUPERUSER_PASSWORD");
    }

    @Override
    public void run(ApplicationArguments args) {
        AppUser superuser = userRepository.findByEmailIgnoreCase(username)
            .orElseGet(() -> new AppUser(username, "", UserRole.superuser, "Superusuario"));

        superuser.setEmail(username);
        superuser.setPassword(passwordEncoder.encode(password));
        superuser.setRole(UserRole.superuser);
        superuser.setName("Superusuario");
        userRepository.save(superuser);
    }

    private static String requireValue(String value, String environmentVariable) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(environmentVariable + " must not be blank");
        }
        return value.trim();
    }
}
