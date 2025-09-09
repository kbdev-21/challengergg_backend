package com.example.challengergg.dto

import com.example.challengergg.entity.analytic.ItemStat
import com.example.challengergg.entity.analytic.MatchUpStat
import com.example.challengergg.entity.analytic.RuneStat
import com.example.challengergg.entity.analytic.SpellComboStat
import com.example.challengergg.enums.ChampTier
import com.example.challengergg.enums.PlayerPosition
import java.util.*

class PlayerChampionStatDto {
    var puuid: String = "";
    var code: String = "";
    var championName: String = "";
    var championDisplayName: String = "";
    var position: PlayerPosition = PlayerPosition.UNK;
    var picks: Int = 0;
    var pickRate: Double = 0.0;
    var wins: Int = 0;
    var winRate: Double = 0.0;
    var avgKills: Double = 0.0;
    var avgDeaths: Double = 0.0;
    var avgAssists: Double = 0.0;
    var avgKda: Double = 0.0;
    var avgKp: Double = 0.0;
    var avgGpm: Double = 0.0;
    var avgCspm: Double = 0.0;
    var avgDpm: Double = 0.0;
    var avgPhysicalDmg: Double = 0.0;
    var avgMagicDmg: Double = 0.0
    var avgTrueDmg: Double = 0.0
    var avgTdpm: Double = 0.0;
    var avgPenta: Double = 0.0;
    var avgSolokills: Double = 0.0;
    var avgKbscore: Double = 0.0;
    var matchUps: List<MatchUpStat> = listOf();
    var createdAt: Date = Date();
}