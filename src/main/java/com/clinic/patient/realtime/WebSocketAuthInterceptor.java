package com.clinic.patient.realtime;

import com.clinic.patient.security.jwt.JwtService;
import com.clinic.patient.user.entity.User;
import com.clinic.patient.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Authenticates the STOMP CONNECT frame with the same JWT used by the REST API.
 * Browsers cannot set headers on the WebSocket handshake itself, so the token
 * travels as an "Authorization" header inside the CONNECT frame.
 */
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String header = accessor.getFirstNativeHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                throw new AccessDeniedException("Authentication required please login");
            }

            String email = jwtService.extractEmail(header.substring(7), "Access");
            if (email == null) {
                throw new AccessDeniedException("Invalid or Expired token");
            }

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new AccessDeniedException("Invalid or Expired token"));

            accessor.setUser(new UsernamePasswordAuthenticationToken(
                    email, null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))));
        }
        return message;
    }
}
