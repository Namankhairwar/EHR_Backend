package com.clinic.patient.notification.controller;

import com.clinic.patient.notification.entity.Notification;
import com.clinic.patient.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PreAuthorize("hasRole('ADMIN') or @accessGuard.isSelf(#patientId)")
    @GetMapping("patient/{patientId}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable("patientId") String patientId) {
        return ResponseEntity.ok(notificationService.getNotificationsForUser(patientId));
    }

    @PutMapping("{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable("id") long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}
