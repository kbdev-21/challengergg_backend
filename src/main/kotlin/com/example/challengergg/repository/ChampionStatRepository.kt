package com.example.challengergg.repository

import com.example.challengergg.entity.analytic.ChampionStat
import org.springframework.data.jpa.repository.JpaRepository

interface ChampionStatRepository: JpaRepository<ChampionStat, String> {
}