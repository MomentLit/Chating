package com.example.chating.service;

import com.example.chating.client.SpaceClient;
import com.example.chating.client.UserClient;
import com.example.chating.client.dto.SpaceDetailResponse;
import com.example.chating.client.dto.UserSearchResponse;
import com.example.chating.dto.request.ChatMessageSendRequest;
import com.example.chating.dto.request.ChatRoomCreateRequest;
import com.example.chating.dto.response.ChatMessageHistorySearchResponse;
import com.example.chating.dto.response.ChatMessageHistorySearchResponses;
import com.example.chating.entity.ChatMessage;
import com.example.chating.entity.ChatRoom;
import com.example.chating.global.exception.BadRequestException;
import com.example.chating.global.exception.ChatRoomNotFoundException;
import com.example.chating.global.exception.ForbiddenException;
import com.example.chating.global.exception.SpaceNotFoundException;
import com.example.chating.repository.ChatMessageRepository;
import com.example.chating.repository.ChatRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private SpaceClient spaceClient;

    @Mock
    private UserClient userClient;

    private ChatService chatService;

    @BeforeEach
    void setUp() {
        chatService = new ChatService(
                chatRoomRepository,
                chatMessageRepository,
                spaceClient,
                userClient
        );
    }

    @Test
    void createChatRoomStoresDenormalizedNames() {
        when(spaceClient.getSpace(1L))
                .thenReturn(new SpaceDetailResponse(1L, "host-1", "연습실"));
        when(chatRoomRepository.findBySpaceIdAndSellerId(1L, "seller-1"))
                .thenReturn(Optional.empty());
        when(userClient.getUser("host-1")).thenReturn(new UserSearchResponse("호스트"));
        when(userClient.getUser("seller-1")).thenReturn(new UserSearchResponse("셀러"));
        when(chatRoomRepository.save(any(ChatRoom.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        chatService.createChatRoom("seller-1", new ChatRoomCreateRequest(1L));

        ArgumentCaptor<ChatRoom> captor = ArgumentCaptor.forClass(ChatRoom.class);
        verify(chatRoomRepository).save(captor.capture());
        ChatRoom saved = captor.getValue();
        assertThat(saved.getHostId()).isEqualTo("host-1");
        assertThat(saved.getSellerId()).isEqualTo("seller-1");
        assertThat(saved.getSpaceName()).isEqualTo("연습실");
        assertThat(saved.getHostName()).isEqualTo("호스트");
        assertThat(saved.getSellerName()).isEqualTo("셀러");
    }

    @Test
    void createChatRoomRejectsHostOwnSpace() {
        when(spaceClient.getSpace(1L))
                .thenReturn(new SpaceDetailResponse(1L, "host-1", "연습실"));

        assertThatThrownBy(() -> chatService.createChatRoom("host-1", new ChatRoomCreateRequest(1L)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("본인 공간에는 채팅방을 생성할 수 없습니다.");

        verify(chatRoomRepository, never()).save(any());
    }

    @Test
    void createChatRoomReturnsExistingRoom() {
        when(spaceClient.getSpace(1L))
                .thenReturn(new SpaceDetailResponse(1L, "host-1", "연습실"));
        when(chatRoomRepository.findBySpaceIdAndSellerId(1L, "seller-1"))
                .thenReturn(Optional.of(chatRoom()));

        chatService.createChatRoom("seller-1", new ChatRoomCreateRequest(1L));

        verify(chatRoomRepository, never()).save(any());
        verify(userClient, never()).getUser(any());
    }

    @Test
    void getMyChatRoomsUsesStoredNamesWithoutClientCalls() {
        when(chatRoomRepository.findByHostIdOrSellerIdOrderByCreatedAtDesc("seller-1", "seller-1"))
                .thenReturn(List.of(chatRoom()));

        var responses = chatService.getMyChatRooms("seller-1");

        assertThat(responses.chatRooms()).hasSize(1);
        assertThat(responses.chatRooms().get(0).space().name()).isEqualTo("연습실");
        assertThat(responses.chatRooms().get(0).host().name()).isEqualTo("호스트");
        assertThat(responses.chatRooms().get(0).seller().name()).isEqualTo("셀러");
        verify(spaceClient, never()).getSpace(any());
        verify(userClient, never()).getUser(any());
    }

    @Test
    void getChatMessagesRejectsNonParticipant() {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom()));

        assertThatThrownBy(() -> chatService.getChatMessages("other", 1L))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("채팅방에 대한 권한이 없습니다.");
    }

    @Test
    void getChatMessagesRejectsMissingChatRoom() {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> chatService.getChatMessages("seller-1", 1L))
                .isInstanceOf(ChatRoomNotFoundException.class)
                .hasMessage("채팅방을 찾을 수 없습니다.");
    }

    @Test
    void getChatMessagesMarksCounterpartMessagesAsRead() {
        ChatMessage fromHost = ChatMessage.create(1L, "host-1", "안녕하세요");
        ChatMessage fromSeller = ChatMessage.create(1L, "seller-1", "네 안녕하세요");

        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom()));
        when(chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(1L))
                .thenReturn(List.of(fromHost, fromSeller));

        ChatMessageHistorySearchResponses responses =
                chatService.getChatMessages("seller-1", 1L);

        assertThat(fromHost.getIsRead()).isTrue();
        assertThat(fromSeller.getIsRead()).isFalse();
        assertThat(responses.messages()).hasSize(2);
        assertThat(responses.messages().get(0).senderName()).isEqualTo("호스트");
        assertThat(responses.messages().get(1).senderName()).isEqualTo("셀러");
        verify(userClient, never()).getUser(any());
    }

    @Test
    void sendMessageRejectsBlankContent() {
        assertThatThrownBy(() -> chatService.sendMessage("seller-1", 1L, new ChatMessageSendRequest(" ")))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("메시지 내용은 비어 있을 수 없습니다.");

        verify(chatMessageRepository, never()).save(any());
    }

    @Test
    void sendMessageRejectsNonParticipant() {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom()));

        assertThatThrownBy(() -> chatService.sendMessage("other", 1L, new ChatMessageSendRequest("안녕")))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("채팅방에 대한 권한이 없습니다.");

        verify(chatMessageRepository, never()).save(any());
    }

    @Test
    void sendMessageRejectsDeletedSpace() {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom()));
        when(spaceClient.getSpace(1L))
                .thenThrow(new SpaceNotFoundException("공간을 찾을 수 없습니다."));

        assertThatThrownBy(() -> chatService.sendMessage("seller-1", 1L, new ChatMessageSendRequest("안녕")))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("삭제된 공간의 채팅방에는 메시지를 보낼 수 없습니다.");

        verify(chatMessageRepository, never()).save(any());
    }

    @Test
    void sendMessageStoresMessageAndReturnsStoredSenderName() {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom()));
        when(spaceClient.getSpace(1L))
                .thenReturn(new SpaceDetailResponse(1L, "host-1", "연습실"));
        when(chatMessageRepository.save(any(ChatMessage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ChatMessageHistorySearchResponse response =
                chatService.sendMessage("seller-1", 1L, new ChatMessageSendRequest("안녕하세요"));

        verify(chatMessageRepository).save(any(ChatMessage.class));
        assertThat(response.senderId()).isEqualTo("seller-1");
        assertThat(response.senderName()).isEqualTo("셀러");
        assertThat(response.content()).isEqualTo("안녕하세요");
        assertThat(response.isRead()).isFalse();
        verify(userClient, never()).getUser(any());
    }

    @Test
    void validateChatRoomParticipantRejectsNonParticipant() {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom()));

        assertThatThrownBy(() -> chatService.validateChatRoomParticipant("other", 1L))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("채팅방에 대한 권한이 없습니다.");
    }

    private ChatRoom chatRoom() {
        return ChatRoom.create(
                1L,
                "host-1",
                "seller-1",
                "연습실",
                "호스트",
                "셀러"
        );
    }
}
