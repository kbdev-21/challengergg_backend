package com.example.challengergg.dto

import com.example.challengergg.common.enums.QueueType
import java.util.*

class MatchDto {
    var matchId: String = "";
    var version: String = "";
    var queue: QueueType = QueueType.UNK;
    var duration: Long = 0;
    var startTimeStamp: Long = 0;
}