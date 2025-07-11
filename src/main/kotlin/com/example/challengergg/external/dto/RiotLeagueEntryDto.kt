package com.example.challengergg.external.dto

data class RiotLeagueEntryDto(
    var puuid: String,
    var queueType: String,
    var tier: String,
    var rank: String,
    var leaguePoints: Int,
    var wins: Int,
    var losses: Int,
)
