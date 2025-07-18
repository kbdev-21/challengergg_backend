package com.example.challengergg.controller

import com.example.challengergg.dto.PlayerDto
import com.example.challengergg.service.PlayerService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class PlayerController(
    private val playerService: PlayerService
) {
    @GetMapping("/api/v1/players/by-riotid/{gameName}/{tagLine}")
    fun getPlayerByGameNameAndTagLine(@PathVariable gameName: String, @PathVariable tagLine: String): PlayerDto {
        return playerService.getPlayerByGameNameAndTagLine(gameName, tagLine);
    }
}