package com.clinic.patient.security;

import com.clinic.patient.user.repositories.RestrictionGrantRepository;
import com.clinic.patient.user.repositories.UserRepository;
import com.clinic.patient.user.state.RestrictionGrantStatus;
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
    private final RestrictionGrantRepository restrictionGrantRepository;

    /** True when the authenticated email belongs to the user with this ehrId. */
    public boolean isSelf(String userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && userRepository.findById(userId)
                .map(u -> u.getEmail().equals(auth.getName()))
                .orElse(false);
    }

    /**
     * True when the patient has APPROVED a restriction grant for the current
     * user that includes the given attribute (e.g. "allergies").
     */
    public boolean hasApprovedAttribute(String patientId, String attribute) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }
        return userRepository.findByEmail(auth.getName())
                .flatMap(accessor -> restrictionGrantRepository
                        .findByPatient_EhrIdAndAccessor_EhrId(patientId, accessor.getEhrId()))
                .filter(grant -> grant.getStatus() == RestrictionGrantStatus.APPROVED)
                .map(grant -> {
                    String attrs = grant.getRestrictedAttributes();
                    if (attrs == null) {
                        return false;
                    }
                    for (String s : attrs.split(",")) {
                        if (s.trim().equalsIgnoreCase(attribute)) {
                            return true;
                        }
                    }
                    return false;
                })
                .orElse(false);
    }
}
