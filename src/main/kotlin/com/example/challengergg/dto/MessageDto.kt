package com.example.challengergg.dto

import com.example.challengergg.enums.Region
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.validation.constraints.NotBlank
import java.util.*

class MessageDto() {
    var id: UUID = UUID.randomUUID();
    var senderId: UUID = UUID.randomUUID();
    @NotBlank
    var senderName: String = "";
    var room: Region = Region.VN;
    @NotBlank
    var content: String = "";
    var sentAt: Date = Date();
}

