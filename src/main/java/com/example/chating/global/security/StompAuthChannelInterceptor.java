package com.example.chating.global.security;

import com.example.chating.global.exception.BadRequestException;
import com.example.chating.global.exception.ForbiddenException;
import com.example.chating.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private static final String CHAT_TOPIC_PREFIX = "/topic/chat/";

    private final JwtProvider jwtProvider;
    private final ChatService chatService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            authenticate(accessor);
        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            validateChatSubscription(accessor);
        }

        return message;
    }

    private void authenticate(StompHeaderAccessor accessor) {
        String token = accessor.getFirstNativeHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new ForbiddenException("인증 토큰이 필요합니다.");
        }

        token = token.substring(7);

        if (!jwtProvider.validateToken(token)) {
            throw new ForbiddenException("유효하지 않은 인증 토큰입니다.");
        }

        String userId = jwtProvider.getUserId(token);
        String role = jwtProvider.getRole(token);

        accessor.setUser(new UsernamePasswordAuthenticationToken(
                userId,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + role))
        ));
    }

    private void validateChatSubscription(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();

        if (destination == null || !destination.startsWith(CHAT_TOPIC_PREFIX)) {
            return;
        }

        Principal user = accessor.getUser();

        if (user == null) {
            throw new ForbiddenException("인증되지 않은 구독 요청입니다.");
        }

        chatService.validateChatRoomParticipant(user.getName(), parseChatRoomId(destination));
    }

    private Long parseChatRoomId(String destination) {
        try {
            return Long.parseLong(destination.substring(CHAT_TOPIC_PREFIX.length()));
        } catch (NumberFormatException e) {
            throw new BadRequestException("잘못된 채팅방 구독 경로입니다.");
        }
    }
}
