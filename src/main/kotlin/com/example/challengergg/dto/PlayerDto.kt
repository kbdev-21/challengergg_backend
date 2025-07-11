package com.example.challengergg.dto

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