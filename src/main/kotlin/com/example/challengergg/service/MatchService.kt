package com.example.challengergg.service

import com.example.challengergg.dto.MatchDto

interface MatchService {
    fun getMatchesByPuuid(puuid: String): List<MatchDto>;
}