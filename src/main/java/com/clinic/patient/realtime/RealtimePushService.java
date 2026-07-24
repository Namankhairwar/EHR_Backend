package com.clinic.patient.realtime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Pushes domain events to a user's private STOMP queues. A failed push must
 * never roll back the DB write that triggered it, so errors are only logged.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RealtimePushService {

    public static final String QUEUE_NOTIFICATIONS = "/queue/notifications";
    public static final String QUEUE_REPORTS = "/queue/reports";
    public static final String QUEUE_MEDICATIONS = "/queue/medications";

    private final SimpMessagingTemplate messagingTemplate;

    public void sendToUser(String email, String queue, Object payload) {
        try {
            messagingTemplate.convertAndSendToUser(email, queue, payload);
        } catch (Exception e) {
            log.warn("Realtime push to {}{} failed: {}", email, queue, e.getMessage());
        }
    }
}
