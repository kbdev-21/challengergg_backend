package com.example.challengergg.service
import com.example.challengergg.dto.MessageDto
import com.example.challengergg.enums.Region

interface ChatService {
    fun processAndPublishMessage(messageDto: MessageDto);

    fun getMessagesByRoom(room: Region): List<MessageDto>;
}