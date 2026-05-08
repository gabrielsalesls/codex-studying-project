package dev.gabrielsales.user_service.user;

import dev.gabrielsales.user_service.common.exception.ResourceConflictException;
import dev.gabrielsales.user_service.common.exception.ResourceNotFoundException;
import dev.gabrielsales.user_service.user.api.UserRequest;
import dev.gabrielsales.user_service.user.api.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse create(UserRequest request) {
        validateUniqueness(request);

        var user = new User(
                request.name(),
                request.cpf(),
                request.email(),
                request.password(),
                request.type()
        );

        var savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id %s not found".formatted(id)));

        return toResponse(user);
    }

    private void validateUniqueness(UserRequest request) {
        if (userRepository.existsByCpf(request.cpf())) {
            throw new ResourceConflictException("CPF already registered");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new ResourceConflictException("Email already registered");
        }
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getType());
    }
}
