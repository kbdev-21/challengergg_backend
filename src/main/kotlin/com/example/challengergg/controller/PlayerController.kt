package com.example.challengergg.controller

import com.example.challengergg.enums.Region
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
    @GetMapping("/api/v1/players/{region}/by-riotid/{gameName}/{tagLine}")
    fun getPlayerByGameNameAndTagLine(
        @PathVariable gameName: String,
        @PathVariable tagLine: String,
        @PathVariable region: String
    ): PlayerDto {
        val regionEnum = Region.valueOf(region.uppercase());
        return playerService.getPlayerByGameNameAndTagLine(gameName, tagLine, regionEnum);
    }
}