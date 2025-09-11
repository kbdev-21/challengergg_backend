package com.example.challengergg.controller

import com.example.challengergg.dto.MessageDto
import com.example.challengergg.enums.Region
import com.example.challengergg.exception.CustomException
import com.example.challengergg.service.ChatService
import jakarta.validation.Valid
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.springframework.http.HttpStatus
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController(
    private val chatService: ChatService,
    private val validator: Validator
) {
    @MessageMapping("/sendMessage")
    fun sendMessage(messageDto: MessageDto) {
        if(validator.validate(messageDto).isNotEmpty()) {
            throw CustomException(HttpStatus.BAD_REQUEST, "Invalid message");
        };

        chatService.processAndPublishMessage(messageDto);
    }

    @GetMapping("/api/v1/messages/{room}")
    fun getMessagesByRoom(@PathVariable room: String): List<MessageDto> {
        val roomAsEnum = Region.valueOf(room.uppercase());
        return chatService.getMessagesByRoom(roomAsEnum);
    }
}