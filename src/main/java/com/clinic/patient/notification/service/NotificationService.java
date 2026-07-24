package com.clinic.patient.notification.service;

import com.clinic.patient.notification.entity.Notification;
import com.clinic.patient.notification.repositories.NotificationRepository;
import com.clinic.patient.realtime.RealtimePushService;
import com.clinic.patient.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final RealtimePushService realtimePushService;

    @Transactional
    public void createNotification(User user, String message) {
        Notification notification = Notification.builder()
                .user(user)
                .message(message)
                .timestamp(LocalDateTime.now())
                .readStatus(false)
                .build();
        notificationRepository.save(notification);
        realtimePushService.sendToUser(user.getEmail(),
                RealtimePushService.QUEUE_NOTIFICATIONS, notification);
    }

    public List<Notification> getNotificationsForUser(String userId) {
        return notificationRepository.findAllByUser_EhrIdOrderByTimestampDesc(userId);
    }

    @Transactional
    public void markAsRead(long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && (auth == null || !notification.getUser().getEmail().equals(auth.getName()))) {
            throw new AccessDeniedException("You can only mark your own notifications as read");
        }

        notification.setReadStatus(true);
        notificationRepository.save(notification);
    }

    /** WebSocket variant: STOMP handlers have no SecurityContext, so identity is passed in. */
    @Transactional
    public void markAsRead(long notificationId, String requesterEmail, boolean isAdmin) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!isAdmin && !notification.getUser().getEmail().equals(requesterEmail)) {
            throw new AccessDeniedException("You can only mark your own notifications as read");
        }

        notification.setReadStatus(true);
        notificationRepository.save(notification);
        // Sync the read state to the owner's other open tabs/devices.
        realtimePushService.sendToUser(notification.getUser().getEmail(),
                RealtimePushService.QUEUE_NOTIFICATIONS, notification);
    }
}
