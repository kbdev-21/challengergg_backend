package com.example.challengergg.repository

import com.example.challengergg.entity.chat.Message
import com.example.challengergg.enums.Region
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import java.util.Date
import java.util.UUID

interface MessageRepository: JpaRepository<Message, UUID> {
    fun findByRoom(room: Region): List<Message>;

    @Query(
        "SELECT * FROM public.messages WHERE sent_at >= :timeLimit AND room = :room",
        nativeQuery = true
    )
    fun findRecentMessagesByRoom(timeLimit: Date, room: String): List<Message>

    @Modifying
    @Transactional
    @Query(
        "DELETE FROM public.messages WHERE sent_at <= :timeLimit",
        nativeQuery = true
    )
    fun deleteMessagesOlderThan(timeLimit: Date)
}