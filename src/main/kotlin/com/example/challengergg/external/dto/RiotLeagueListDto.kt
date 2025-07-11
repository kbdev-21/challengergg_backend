package com.example.challengergg.external.dto

data class RiotLeagueListDto (
    var entries: List<LeagueItemDto>
)

data class LeagueItemDto (
    var puuid: String
)