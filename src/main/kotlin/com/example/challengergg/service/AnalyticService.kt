package com.example.challengergg.service

import com.example.challengergg.dto.ChampionStatDetailDto
import com.example.challengergg.dto.PlayerChampionStatDto

interface AnalyticService {
    fun getAllChampionStats(): List<ChampionStatDetailDto>;

    fun getChampionStatsByChampionName(championName: String): List<ChampionStatDetailDto>;

    fun updateChampionStats();

    fun getPlayerChampionStats(puuid: String): List<PlayerChampionStatDto>;

    fun updatePlayerChampionStats(puuid: String);
}