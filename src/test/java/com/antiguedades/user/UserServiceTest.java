package com.antiguedades.user;

import com.antiguedades.exception.BusinessException;
import com.antiguedades.user.dto.CreateUserRequest;
import com.antiguedades.user.dto.UpdateUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final UserService userService = new UserService(userRepository, passwordEncoder);

    @Test
    void hidesSuperuserFromUserList() {
        AppUser superuser = user("root@example.com", UserRole.superuser);
        AppUser admin = user("admin@example.com", UserRole.admin);
        when(userRepository.findAll()).thenReturn(List.of(superuser, admin));

        assertThat(userService.getAllUsers())
            .extracting(response -> response.email())
            .containsExactly("admin@example.com");
    }

    @Test
    void rejectsCreatingAnotherSuperuser() {
        CreateUserRequest request = new CreateUserRequest(
            "Root", "root2@example.com", "password", UserRole.superuser);

        assertThatThrownBy(() -> userService.createUser(request))
            .isInstanceOf(BusinessException.class);
        verifyNoInteractions(userRepository, passwordEncoder);
    }

    @Test
    void rejectsPromotingAnotherUserToSuperuser() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.of(user("user@example.com", UserRole.user)));

        assertThatThrownBy(() -> userService.updateUser(
            id, new UpdateUserRequest(null, null, null, UserRole.superuser)))
            .isInstanceOf(BusinessException.class);
        verify(userRepository, never()).save(any());
    }

    @Test
    void rejectsUpdatingOrDeletingSuperuser() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.of(user("root@example.com", UserRole.superuser)));

        assertThatThrownBy(() -> userService.updateUser(
            id, new UpdateUserRequest("Changed", null, null, null)))
            .isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> userService.deleteUser(id))
            .isInstanceOf(BusinessException.class);
        verify(userRepository, never()).save(any());
        verify(userRepository, never()).delete(any());
    }

    private AppUser user(String email, UserRole role) {
        return new AppUser(email, "bcrypt-hash", role, "Name");
    }
}
