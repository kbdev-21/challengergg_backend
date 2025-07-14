package com.example.challengergg.dto

import com.example.challengergg.common.enums.ChampTier
import com.example.challengergg.common.enums.PlayerPosition
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

class ChampionStatSummaryDto {
    var code: String = "";
    var championName: String = "";
    var championDisplayName: String = "";
    var position: PlayerPosition = PlayerPosition.UNK;
    var picks: Int = 0;
    var pickRate: Double = 0.0;
    var wins: Int = 0;
    var winRate: Double = 0.0;
    var power: Int = 0;
    var tier: ChampTier = ChampTier.S;
    var avgKills: Double = 0.0;
    var avgDeaths: Double = 0.0;
    var avgAssists: Double = 0.0;
    var avgKda: Double = 0.0;
    var avgKp: Double = 0.0;
    var avgGpm: Double = 0.0;
    var avgCspm: Double = 0.0;
    var avgDpm: Double = 0.0;
    var avgTdpm: Double = 0.0;
    var avgPenta: Double = 0.0;
    var avgSolokills: Double = 0.0;
    var avgKbscore: Double = 0.0;
}