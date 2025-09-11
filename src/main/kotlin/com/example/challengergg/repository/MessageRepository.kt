package com.example.challengergg.repository

import com.example.challengergg.entity.chat.Message
import com.example.challengergg.enums.Region
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MessageRepository: JpaRepository<Message, UUID> {
    fun findByRoom(room: Region): List<Message>;
}