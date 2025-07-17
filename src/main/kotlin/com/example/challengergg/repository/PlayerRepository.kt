package com.example.challengergg.repository

import com.example.challengergg.entity.Player
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface PlayerRepository: JpaRepository<Player, UUID> {
    fun findByPuuid(puuid: String): Player?;

    @Query(
        """
            SELECT * FROM players
        WHERE LOWER(search_name) LIKE LOWER(CONCAT('%', :key, '%'))
        ORDER BY
            CASE
                WHEN LOWER(search_name) LIKE LOWER(CONCAT(:key, '%')) THEN 0
                ELSE 1
            END,
            updated_at DESC
        """,
        nativeQuery = true
    )
    fun searchForSearchNameByKey(key: String): List<Player>;
}