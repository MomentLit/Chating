package com.example.chating.entity;

import com.example.chating.global.exception.ForbiddenException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "chat_rooms")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "space_id")
    private Long spaceId;

    @Column(nullable = false, name = "host_id")
    private String hostId;

    @Column(nullable = false, name = "seller_id")
    private String sellerId;

    @Column(nullable = false, name = "space_name")
    private String spaceName;

    @Column(nullable = false, name = "host_name")
    private String hostName;

    @Column(nullable = false, name = "seller_name")
    private String sellerName;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    public static ChatRoom create(
            Long spaceId,
            String hostId,
            String sellerId,
            String spaceName,
            String hostName,
            String sellerName
    ) {
        return ChatRoom.builder()
                .spaceId(spaceId)
                .hostId(hostId)
                .sellerId(sellerId)
                .spaceName(spaceName)
                .hostName(hostName)
                .sellerName(sellerName)
                .build();
    }

    public boolean isParticipant(String userId) {
        return hostId.equals(userId) || sellerId.equals(userId);
    }

    public void validateParticipant(String userId) {
        if (!isParticipant(userId)) {
            throw new ForbiddenException("채팅방에 대한 권한이 없습니다.");
        }
    }

    public String getParticipantName(String userId) {
        if (hostId.equals(userId)) {
            return hostName;
        }
        if (sellerId.equals(userId)) {
            return sellerName;
        }
        return null;
    }
}
