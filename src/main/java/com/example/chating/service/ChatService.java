package com.example.chating.service;

import com.example.chating.client.SpaceClient;
import com.example.chating.client.UserClient;
import com.example.chating.client.dto.SpaceDetailResponse;
import com.example.chating.dto.request.ChatMessageSendRequest;
import com.example.chating.dto.request.ChatRoomCreateRequest;
import com.example.chating.dto.response.ChatMessageHistorySearchResponse;
import com.example.chating.dto.response.ChatMessageHistorySearchResponses;
import com.example.chating.dto.response.ChatRoomCreateResponse;
import com.example.chating.dto.response.ChatRoomListSearchResponse;
import com.example.chating.dto.response.ChatRoomListSearchResponses;
import com.example.chating.entity.ChatMessage;
import com.example.chating.entity.ChatRoom;
import com.example.chating.global.exception.BadRequestException;
import com.example.chating.global.exception.ChatRoomNotFoundException;
import com.example.chating.global.exception.SpaceNotFoundException;
import com.example.chating.repository.ChatMessageRepository;
import com.example.chating.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final SpaceClient spaceClient;
    private final UserClient userClient;

    @Transactional
    public ChatRoomCreateResponse createChatRoom(String userId, ChatRoomCreateRequest request) {
        SpaceDetailResponse space = spaceClient.getSpace(request.spaceId());

        if (space.hostId().equals(userId)) {
            throw new BadRequestException("본인 공간에는 채팅방을 생성할 수 없습니다.");
        }

        ChatRoom chatRoom = chatRoomRepository
                .findBySpaceIdAndSellerId(request.spaceId(), userId)
                .orElseGet(() -> chatRoomRepository.save(
                        ChatRoom.create(
                                request.spaceId(),
                                space.hostId(),
                                userId,
                                space.name(),
                                userClient.getUser(space.hostId()).name(),
                                userClient.getUser(userId).name()
                        )
                ));

        return ChatRoomCreateResponse.from(chatRoom);
    }

    @Transactional(readOnly = true)
    public ChatRoomListSearchResponses getMyChatRooms(String userId) {
        List<ChatRoom> chatRooms =
                chatRoomRepository.findByHostIdOrSellerIdOrderByCreatedAtDesc(userId, userId);

        List<ChatRoomListSearchResponse> responses = chatRooms.stream()
                .map(ChatRoomListSearchResponse::from)
                .toList();

        return new ChatRoomListSearchResponses(responses);
    }

    @Transactional
    public ChatMessageHistorySearchResponses getChatMessages(String userId, Long chatRoomId) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        chatRoom.validateParticipant(userId);

        List<ChatMessage> messages =
                chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoomId);

        messages.stream()
                .filter(message -> !message.isSentBy(userId) && !message.getIsRead())
                .forEach(ChatMessage::markRead);

        List<ChatMessageHistorySearchResponse> responses = messages.stream()
                .map(message -> ChatMessageHistorySearchResponse.from(
                        message,
                        chatRoom.getParticipantName(message.getSenderId())
                ))
                .toList();

        return new ChatMessageHistorySearchResponses(chatRoomId, responses);
    }

    @Transactional
    public ChatMessageHistorySearchResponse sendMessage(
            String userId,
            Long chatRoomId,
            ChatMessageSendRequest request
    ) {
        if (request.content() == null || request.content().isBlank()) {
            throw new BadRequestException("메시지 내용은 비어 있을 수 없습니다.");
        }

        ChatRoom chatRoom = getChatRoom(chatRoomId);
        chatRoom.validateParticipant(userId);
        validateSpaceExists(chatRoom.getSpaceId());

        ChatMessage message = chatMessageRepository.save(
                ChatMessage.create(chatRoomId, userId, request.content())
        );

        return ChatMessageHistorySearchResponse.from(
                message,
                chatRoom.getParticipantName(userId)
        );
    }

    @Transactional(readOnly = true)
    public void validateChatRoomParticipant(String userId, Long chatRoomId) {
        getChatRoom(chatRoomId).validateParticipant(userId);
    }

    private ChatRoom getChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("채팅방을 찾을 수 없습니다."));
    }

    private void validateSpaceExists(Long spaceId) {
        try {
            spaceClient.getSpace(spaceId);
        } catch (SpaceNotFoundException e) {
            throw new BadRequestException("삭제된 공간의 채팅방에는 메시지를 보낼 수 없습니다.");
        }
    }
}
