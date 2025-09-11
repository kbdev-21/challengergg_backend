package com.example.challengergg.entity.chat

import com.example.challengergg.enums.Region
import jakarta.persistence.*
import java.util.Date
import java.util.UUID

@Entity
@Table(name = "messages")
class Message {
    @Id
    var id: UUID = UUID.randomUUID();

    var senderId: UUID = UUID.randomUUID();

    var senderName: String = "";

    @Enumerated(EnumType.STRING)
    var room: Region = Region.VN;

    var content: String = "";

    var sentAt: Date = Date();
}