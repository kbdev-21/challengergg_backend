package com.example.challengergg.repository

import com.example.challengergg.entity.analytic.PlayerChampionStat

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.transaction.annotation.Transactional

interface PlayerChampionStatRepository: JpaRepository<PlayerChampionStat, String> {
    fun findByPuuid(puuid: String): List<PlayerChampionStat>;

    @Transactional
    @Modifying
    fun deleteByPuuid(puuid: String);
}