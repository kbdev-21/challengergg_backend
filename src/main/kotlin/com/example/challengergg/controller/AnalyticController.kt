package com.example.challengergg.controller

import com.example.challengergg.dto.ChampionStatDetailDto
import com.example.challengergg.dto.ChampionStatSummaryDto
import com.example.challengergg.service.AnalyticService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AnalyticController(
    private val analyticService: AnalyticService
) {
    @PostMapping("/api/v1/analytics/champstats/update")
    fun updateChampStats(): String {
        analyticService.updateChampionStats();
        return "Updated successfully";
    }

    @GetMapping("/api/v1/analytics/champstats")
    fun getChampStats(): List<ChampionStatSummaryDto> {
        return analyticService.getAllChampionStats();
    }

    @GetMapping("/api/v1/analytics/champstats/by-championname/{championName}")
    fun getChampStatsByChampionName(@PathVariable championName: String): List<ChampionStatDetailDto> {
        return analyticService.getChampionStatsByChampionName(championName);
    }
}