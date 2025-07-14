package com.example.challengergg.dto

import com.example.challengergg.common.enums.QueueType
import com.example.challengergg.common.enums.RankDivision
import com.example.challengergg.common.enums.RankTier
import java.util.Date

class PlayerDto {
    var puuid: String = "";
    var gameName: String = "";
    var tagLine: String = "";
    var searchName: String = "";
    var profileIconId: Int = 1;
    var summonerLevel: Long = 1;
    var ranks: List<RankDto> = listOf();
    var createdAt: Date = Date();
    var updatedAt: Date = Date();
}

class RankDto {
    var queue: QueueType = QueueType.UNK;
    var tier: RankTier = RankTier.UNK;
    var division: RankDivision = RankDivision.I;
    var points: Int = 0;
    var wins: Int = 0;
    var losses: Int = 0;
    var winRate: Double = 0.0;
    var power: Int = 0;
}