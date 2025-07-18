package com.example.challengergg.controller

import com.example.challengergg.dto.MatchDto
import com.example.challengergg.service.MatchService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MatchController(
    private val matchService: MatchService
) {
    @GetMapping("/api/v1/matches/by-puuid/{puuid}")
    suspend fun getMatchesByPuuid(
        @PathVariable puuid: String,
        @RequestParam start: Int?,
        @RequestParam count: Int?
    ): List<MatchDto> {
        return matchService.getMatchesByPuuid(puuid, start ?: 0, count ?: 20);
    }

    @GetMapping("/api/v1/matches/by-riotid/{gameName}/{tagLine}")
    suspend fun getMatchesByRiotId(
        @PathVariable gameName: String,
        @PathVariable tagLine: String,
        @RequestParam start: Int?,
        @RequestParam count: Int?
    ): List<MatchDto> {
        return matchService.getMatchesByGameNameAndTagLine(gameName, tagLine, start ?: 0, count ?: 20);
    }

}