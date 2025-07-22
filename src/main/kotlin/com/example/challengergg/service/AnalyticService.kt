package com.example.challengergg.service

import com.example.challengergg.dto.ChampionStatDetailDto
import com.example.challengergg.dto.ChampionStatSummaryDto

interface AnalyticService {
    fun getAllChampionStats(): List<ChampionStatDetailDto>;

    fun getChampionStatsByChampionName(championName: String): List<ChampionStatDetailDto>;

    fun updateChampionStats();
}