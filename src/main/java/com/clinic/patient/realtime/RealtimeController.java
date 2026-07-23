package com.clinic.patient.realtime;

import com.clinic.patient.medication.dto.MedicineRequest;
import com.clinic.patient.medication.service.MedicineService;
import com.clinic.patient.notification.service.NotificationService;
import com.clinic.patient.realtime.dto.MarkReadRequest;
import com.clinic.patient.report.dto.ReportRequestDto;
import com.clinic.patient.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Arrays;

/**
 * STOMP write endpoints: clients publish to /app/<destination>, the data is
 * persisted through the existing services, which then broadcast the saved
 * entity back out on the owner's /user/queue/* destinations.
 *
 * Method security relies on the Principal set at CONNECT by
 * {@link WebSocketAuthInterceptor}; @PreAuthorize is not evaluated on the
 * STOMP channel, so roles are checked explicitly here.
 */
@Controller
@RequiredArgsConstructor
public class RealtimeController {

    private final NotificationService notificationService;
    private final MedicineService medicineService;
    private final ReportService reportService;

    @MessageMapping("notifications/read")
    public void markNotificationRead(@Payload MarkReadRequest request, Principal principal) {
        notificationService.markAsRead(request.getId(), principal.getName(), hasRole(principal, "ROLE_ADMIN"));
    }

    @MessageMapping("medications/add")
    public void addMedication(@Payload MedicineRequest request, Principal principal) {
        requireAnyRole(principal, "ROLE_DOCTOR", "ROLE_ADMIN");
        medicineService.save(request);
    }

    @MessageMapping("reports/add")
    public void addReport(@Payload ReportRequestDto request, Principal principal) {
        requireAnyRole(principal, "ROLE_DOCTOR", "ROLE_ADMIN");
        reportService.addReport(request);
    }

    @MessageExceptionHandler
    @SendToUser(destinations = "/queue/errors", broadcast = false)
    public String handleException(Exception e) {
        return e.getMessage();
    }

    private boolean hasRole(Principal principal, String role) {
        return principal instanceof Authentication auth
                && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    }

    private void requireAnyRole(Principal principal, String... roles) {
        if (Arrays.stream(roles).noneMatch(r -> hasRole(principal, r))) {
            throw new AccessDeniedException("You are not allowed to perform this action");
        }
    }
}
