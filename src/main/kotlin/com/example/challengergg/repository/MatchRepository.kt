package com.example.challengergg.repository

import com.example.challengergg.entity.Match
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface MatchRepository: JpaRepository<Match, UUID> {
    fun findByMatchId(matchId: String): Match?;

    fun findByMatchIdIn(matchIds: List<String>): List<Match>;

    /* custom queries */
    @Query(
        """
        SELECT COUNT(*)
        FROM public.matches m
        WHERE m.queue IN ('SOLO', 'FLEX')    
        """,
        nativeQuery = true
    )
    fun countRankedMatches(): Int;
}