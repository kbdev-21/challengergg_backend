package com.example.challengergg.service.impl

import com.example.challengergg.dto.MessageDto
import com.example.challengergg.entity.chat.Message
import com.example.challengergg.enums.Region
import com.example.challengergg.repository.MessageRepository
import com.example.challengergg.service.ChatService
import org.modelmapper.ModelMapper
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.util.Date

@Service
class ChatServiceImpl(
    private val messagingTemplate: SimpMessagingTemplate,
    private val messageRepository: MessageRepository
): ChatService {
    override fun processAndPublishMessage(messageDto: MessageDto) {
        val message = Message().apply {
            id = messageDto.id;
            senderId = messageDto.senderId;
            senderName = messageDto.senderName;
            content = messageDto.content;
            room = messageDto.room;
            sentAt = messageDto.sentAt;
        }
        messageRepository.save(message);
        messagingTemplate.convertAndSend("/chat", messageDto);
    }

    override fun getMessagesByRoom(room: Region): List<MessageDto> {
        val modelMapper = ModelMapper();

        val messages = messageRepository.findByRoom(room);
        val twentyFourHoursAgo = Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);

        return messages
            .filter { m ->  m.sentAt.after(twentyFourHoursAgo) }
            .map {
                m -> modelMapper.map(m, MessageDto::class.java)
            }
    }

}