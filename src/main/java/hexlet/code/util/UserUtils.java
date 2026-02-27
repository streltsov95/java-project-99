package hexlet.code.util;

import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserUtils {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        var email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));
    }

    public boolean isCurrentUser(long userId) {
        var userEmail = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"))
                .getEmail();
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return userEmail.equals(authentication.getName());
    }
}
