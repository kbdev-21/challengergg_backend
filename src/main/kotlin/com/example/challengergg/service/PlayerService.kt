package com.example.challengergg.service

import com.example.challengergg.enums.Region
import com.example.challengergg.dto.PlayerDto

interface PlayerService {
    fun getPlayerByGameNameAndTagLine(gameName: String, tagLine: String, region: Region): PlayerDto;
}