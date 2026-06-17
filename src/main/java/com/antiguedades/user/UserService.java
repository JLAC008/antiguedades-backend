package com.antiguedades.user;

import com.antiguedades.exception.BusinessException;
import com.antiguedades.exception.ResourceNotFoundException;
import com.antiguedades.user.dto.CreateUserRequest;
import com.antiguedades.user.dto.UpdateUserRequest;
import com.antiguedades.user.dto.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserService {
    private static final String SUPERUSER_EMAIL = "superusuario@gmail.com";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
            .filter(user -> !isSuperuser(user))
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new BusinessException("Ya existe un usuario con ese correo");
        }
        AppUser user = new AppUser(
            request.email().toLowerCase().trim(),
            passwordEncoder.encode(request.password()),
            request.role(),
            request.name().trim()
        );
        user = userRepository.save(user);
        return toResponse(user);
    }

    @Transactional
    public UserResponse updateUser(UUID id, UpdateUserRequest request) {
        AppUser user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        if (isSuperuser(user)) {
            throw new BusinessException("El superusuario solo se puede modificar desde base de datos");
        }

        if (request.name() != null) user.setName(request.name().trim());
        if (request.email() != null) {
            var existing = userRepository.findByEmailIgnoreCase(request.email());
            if (existing.isPresent() && !existing.get().getId().equals(id)) {
                throw new BusinessException("Ya existe otro usuario con ese correo");
            }
            user.setEmail(request.email().toLowerCase().trim());
        }
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }
        if (request.role() != null) user.setRole(request.role());

        user = userRepository.save(user);
        return toResponse(user);
    }

    @Transactional
    public void deleteUser(UUID id) {
        AppUser user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        if (isSuperuser(user)) {
            throw new BusinessException("El superusuario solo se puede eliminar desde base de datos");
        }
        userRepository.delete(user);
    }

    private UserResponse toResponse(AppUser user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getRole(), user.getName(), user.getCreatedAt());
    }

    private boolean isSuperuser(AppUser user) {
        return user.getEmail() != null && user.getEmail().equalsIgnoreCase(SUPERUSER_EMAIL);
    }
}
