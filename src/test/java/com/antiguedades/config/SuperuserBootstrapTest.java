package com.antiguedades.config;

import com.antiguedades.user.AppUser;
import com.antiguedades.user.UserRepository;
import com.antiguedades.user.UserRole;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SuperuserBootstrapTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    @Test
    void createsSuperuserWithEncodedPasswordWhenMissing() throws Exception {
        when(userRepository.findByEmailIgnoreCase("admin@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("strong-password")).thenReturn("bcrypt-hash");
        SuperuserBootstrap bootstrap = new SuperuserBootstrap(
            userRepository, passwordEncoder, " Admin@Example.com ", "strong-password");

        bootstrap.run(null);

        ArgumentCaptor<AppUser> userCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(userRepository).save(userCaptor.capture());
        AppUser user = userCaptor.getValue();
        assertThat(user.getEmail()).isEqualTo("admin@example.com");
        assertThat(user.getPassword()).isEqualTo("bcrypt-hash");
        assertThat(user.getRole()).isEqualTo(UserRole.superuser);
        assertThat(user.getName()).isEqualTo("Superusuario");
    }

    @Test
    void synchronizesExistingAccountOnEveryStartup() throws Exception {
        AppUser existing = new AppUser("admin@example.com", "old-hash", UserRole.admin, "Old name");
        when(userRepository.findByEmailIgnoreCase("admin@example.com")).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("rotated-password")).thenReturn("new-bcrypt-hash");
        SuperuserBootstrap bootstrap = new SuperuserBootstrap(
            userRepository, passwordEncoder, "admin@example.com", "rotated-password");

        bootstrap.run(null);

        assertThat(existing.getPassword()).isEqualTo("new-bcrypt-hash");
        assertThat(existing.getRole()).isEqualTo(UserRole.superuser);
        assertThat(existing.getName()).isEqualTo("Superusuario");
        verify(userRepository).save(existing);
    }
}
