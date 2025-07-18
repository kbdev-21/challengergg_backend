package com.example.challengergg.service

import com.example.challengergg.dto.MatchDto

interface MatchService {
    suspend fun getMatchesByPuuid(puuid: String, start: Int, count: Int): List<MatchDto>;

    suspend fun getMatchesByGameNameAndTagLine(gameName: String, tagLine: String, start: Int, count: Int): List<MatchDto>;
}