package com.example.chating.repository;

import com.example.chating.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findBySpaceIdAndSellerId(Long spaceId, String sellerId);

    List<ChatRoom> findByHostIdOrSellerIdOrderByCreatedAtDesc(String hostId, String sellerId);
}
