package com.example.challengergg.dto

import com.example.challengergg.common.enums.RankDivision
import com.example.challengergg.common.enums.QueueType
import com.example.challengergg.common.enums.RankTier

class RankDto {
    var playerPuuid: String = "";
    var queue: QueueType = QueueType.UNK;
    var tier: RankTier = RankTier.UNK;
    var division: RankDivision = RankDivision.I;
    var points: Int = 0;
    var wins: Int = 0;
    var losses: Int = 0;
    var power: Int = 0;
}