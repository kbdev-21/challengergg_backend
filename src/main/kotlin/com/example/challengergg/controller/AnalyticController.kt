package com.example.challengergg.controller

import com.example.challengergg.service.AnalyticService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AnalyticController(
    private val analyticService: AnalyticService
) {
    @PostMapping("/api/v1/analytics/update")
    fun updateChampStats(): String {
        analyticService.updateChampionStats();
        return "OK";
    }

    @GetMapping("/api/v1/analytics/champstats/by-version/{version}")
    fun getChampStats(@PathVariable version: String): String {
        return "Yes sir";
    }
}