package com.example.challengergg.controller

import com.example.challengergg.dto.MatchDto
import com.example.challengergg.service.MatchService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class MatchController(
    private val matchService: MatchService
) {
    @GetMapping("/api/v1/matches/by-puuid/{puuid}")
    fun getMatchesByPuuid(@PathVariable puuid: String): List<MatchDto> {
        return matchService.getMatchesByPuuid(puuid);
    }

    @GetMapping("/api/v1/matches/by-riotid/{gameName}/{tagLine}")
    fun getMatchesByRiotId(@PathVariable gameName: String, @PathVariable tagLine: String): List<MatchDto> {
        return matchService.getMatchesByGameNameAndTagLine(gameName, tagLine);
    }

}