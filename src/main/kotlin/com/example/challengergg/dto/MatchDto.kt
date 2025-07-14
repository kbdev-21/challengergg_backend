package com.example.challengergg.dto

import com.example.challengergg.common.enums.PlayerPosition
import com.example.challengergg.common.enums.QueueType
import com.example.challengergg.common.enums.TeamCode
import com.example.challengergg.entity.Match
import com.example.challengergg.entity.PerformanceItem
import com.example.challengergg.entity.Rune
import jakarta.persistence.*
import java.util.*

class MatchDto {
    var matchId: String = "";
    var version: String = "";
    var queue: QueueType = QueueType.UNK;
    var duration: Long = 0;
    var startTimeStamp: Long = 0;
    var performances: MutableList<PerformanceDto> = mutableListOf();
}

class PerformanceDto {
    var puuid: String = "";
    var gameName: String = "";
    var tagLine: String = "";
    var team: TeamCode = TeamCode.BLUE;
    var win: Boolean = false;
    var position: PlayerPosition = PlayerPosition.UNK;
    var championName: String = "";
    var championDisplayName: String = "";
    var champPosCode: String = "";
    var championLevel: Int = 0;
    var spell1Id: Int = 0;
    var spell2Id: Int = 0;
    var runeMain: Int = 0;
    var runeMainStyle: Int = 0;
    var runeSubStyle: Int = 0;
    var runeSelections: MutableList<Int> = mutableListOf();
    var kills: Int = 0;
    var deaths: Int = 0;
    var assists: Int = 0;
    var kda: Double = 0.0;
    var killParticipation: Double = 0.0;
    var totalCs: Int = 0;
    var totalGold: Int = 0;
    var totalDamageDealt: Int = 0;
    var magicDamageDealt: Int = 0;
    var physicalDamageDealt: Int = 0;
    var trueDamageDealt: Int = 0;
    var totalDamageTaken: Int = 0;
    var totalTurretDamageDealt: Int = 0;
    var itemIds: MutableList<Int> = mutableListOf();
    var soloKills: Int = 0;
    var doubleKills: Int = 0;
    var tripleKills: Int = 0;
    var quadraKills: Int = 0;
    var pentaKills: Int = 0;
    var visionScore: Int = 0;
    var wardsKilled: Int = 0;
    var wardsPlaced: Int = 0;
    var pinkWardsPlaced: Int = 0;
    var kbScore: Int = 0;
    var kbScorePlacement: Int = 0;
    var mvp: Boolean = false;
    var svp: Boolean = false;
    var laneOpponentChampionName: String? = null;
    var laneOpponentChampionDisplayName: String? = null;
    var goldPerMin: Int = 0;
    var csPerMin: Double = 0.0;
    var damagePerMin: Int = 0;
    var turretDamagePerMin: Int = 0;
}