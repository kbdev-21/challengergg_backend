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
        AND m.version = :version
        """,
        nativeQuery = true
    )
    fun countRankedMatches(version: String): Int;

    @Query(
        """
        SELECT COUNT(*)
        FROM public.performances p
        JOIN public.matches m ON p.match_id = m.match_id
        WHERE m.queue IN ('SOLO', 'FLEX')
        AND p.puuid = :puuid  
        """,
        nativeQuery = true
    )
    fun countPlayerRankedMatches(puuid: String): Int;
}