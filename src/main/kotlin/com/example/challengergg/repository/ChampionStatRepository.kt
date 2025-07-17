package com.example.challengergg.repository

import com.example.challengergg.entity.analytic.ChampionStat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ChampionStatRepository: JpaRepository<ChampionStat, String> {
    fun findByChampionName(championName: String): List<ChampionStat>?;

    /* custom queries */
    @Query(
        """
        SELECT * FROM champion_stats
        WHERE LOWER(champion_name) LIKE LOWER(CONCAT('%', :key, '%'))
        OR LOWER(champion_display_name) LIKE LOWER(CONCAT('%', :key, '%'))
        ORDER BY
        CASE
        WHEN LOWER(champion_name) LIKE LOWER(CONCAT(:key, '%')) THEN 0
        WHEN LOWER(champion_display_name) LIKE LOWER(CONCAT(:key, '%')) THEN 1
        ELSE 2
        END,
        power DESC
        """,
        nativeQuery = true
    )
    fun searchForNameOrDisplayNameByKey(key: String): List<ChampionStat>
}