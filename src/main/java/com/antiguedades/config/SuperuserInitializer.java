package com.antiguedades.config;

import com.antiguedades.user.AppUser;
import com.antiguedades.user.UserRepository;
import com.antiguedades.user.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SuperuserInitializer implements ApplicationRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String username;
    private final String password;

    public SuperuserInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.username}") String username,
            @Value("${app.admin.password}") String password) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run(ApplicationArguments args) {
        AppUser superuser = userRepository.findByEmailIgnoreCase(username)
            .orElseGet(() -> new AppUser(username, "pending", UserRole.admin, "Administrador"));

        superuser.setEmail(username);
        superuser.setPassword(passwordEncoder.encode(password));
        superuser.setRole(UserRole.admin);
        superuser.setName("Administrador");
        userRepository.save(superuser);
    }
}
