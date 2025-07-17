package com.example.challengergg.service

import com.example.challengergg.dto.SearchAllResultDto

interface SearchingService {
    fun searchAll(key: String): SearchAllResultDto;
}