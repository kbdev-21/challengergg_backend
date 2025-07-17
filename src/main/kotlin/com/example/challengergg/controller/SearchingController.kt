package com.example.challengergg.controller

import com.example.challengergg.dto.PlayerDto
import com.example.challengergg.dto.SearchAllResultDto
import com.example.challengergg.service.SearchingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchingController(
    private val searchingService: SearchingService
) {
    @GetMapping("/api/v1/search")
    fun searchAll(@RequestParam key: String): SearchAllResultDto {
        return searchingService.searchAll(key);
    }
}