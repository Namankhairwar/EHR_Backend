package com.clinic.patient.security;

import com.clinic.patient.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Used from @PreAuthorize expressions, e.g.
 * "hasRole('ADMIN') or @accessGuard.isSelf(#id)".
 */
@Component("accessGuard")
@AllArgsConstructor
public class AccessGuard {

    private final UserRepository userRepository;

    /** True when the authenticated email belongs to the user with this ehrId. */
    public boolean isSelf(String userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && userRepository.findById(userId)
                .map(u -> u.getEmail().equals(auth.getName()))
                .orElse(false);
    }
}
