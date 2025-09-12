package com.example.challengergg.service

import com.example.challengergg.enums.Region
import com.example.challengergg.dto.MatchDto

interface MatchService {
    suspend fun getMatchesByPuuid(puuid: String, queue: Int?, start: Int, count: Int, region: Region): List<MatchDto>;

    suspend fun getMatchesByGameNameAndTagLine(gameName: String, tagLine: String, queue: Int?, start: Int, count: Int, region: Region): List<MatchDto>;

    fun deleteMatchByVersionIsNot(version: String);
}