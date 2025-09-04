package com.example.challengergg.repository

import com.example.challengergg.enums.ItemType
import com.example.challengergg.entity.Performance
import com.example.challengergg.entity.query.ChampionAvgStatsTable
import com.example.challengergg.entity.query.CountAndWinsTable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface PerformanceRepository: JpaRepository<Performance, UUID> {
    fun findByChampPosCode(champPosCode: String): List<Performance>?;

    /* custom queries */
    @Query(
        """
        SELECT p.champ_pos_code AS value, COUNT(*) AS count, COUNT(CASE WHEN p.win = true THEN 1 END) AS wins
        FROM public.performances p
        JOIN public.matches m ON p.match_id = m.match_id
        WHERE m.queue IN ('SOLO', 'FLEX')
        AND p.position != 'UNK'
        GROUP BY p.champ_pos_code
        ORDER BY p.champ_pos_code ASC
        """,
        nativeQuery = true
    )
    fun countAllRankedChampPosCodes(): List<CountAndWinsTable<String>>;

    @Query(
        """
        SELECT p.spell_combo_code AS value, COUNT(*) AS count, COUNT(CASE WHEN p.win = true THEN 1 END) AS wins
        FROM public.performances p
        JOIN public.matches m ON p.match_id = m.match_id
        WHERE m.queue IN ('SOLO', 'FLEX')
        AND p.champ_pos_code = :champPosCode
        GROUP BY p.spell_combo_code
        ORDER BY count DESC
        """,
        nativeQuery = true
    )
    fun countAllRankedSpellComboCodesByChampPosCode(champPosCode: String): List<CountAndWinsTable<String>>;

    @Query(
        """
            SELECT r.code as value, COUNT(*) AS count, COUNT(CASE WHEN p.win = true THEN 1 END) AS wins
            FROM public.performances p
            JOIN public.runes r ON r.rune_id = p.rune_id
            JOIN public.matches m ON p.match_id = m.match_id
            WHERE m.queue IN ('SOLO', 'FLEX')
            AND p.champ_pos_code = :champPosCode
            GROUP BY r.code
            ORDER BY count DESC
        """,
        nativeQuery = true
    )
    fun countAllRankedRuneCodesByChampPosCode(champPosCode: String): List<CountAndWinsTable<String>>;

    @Query(
        """
            SELECT r.main as value, COUNT(*) AS count, COUNT(CASE WHEN p.win = true THEN 1 END) AS wins
            FROM public.performances p
            JOIN public.runes r ON r.rune_id = p.rune_id
            JOIN public.matches m ON p.match_id = m.match_id
            WHERE m.queue IN ('SOLO', 'FLEX')
            AND p.champ_pos_code = :champPosCode
            GROUP BY r.main
            ORDER BY count DESC
        """,
        nativeQuery = true
    )
    fun countAllRankedRuneMainByChampPosCode(champPosCode: String): List<CountAndWinsTable<Int>>;

    @Query(
        """
            SELECT r.selections as value, COUNT(*) AS count, COUNT(CASE WHEN p.win = true THEN 1 END) AS wins
            FROM public.performances p
            JOIN public.runes r ON r.rune_id = p.rune_id
            JOIN public.matches m ON p.match_id = m.match_id
            WHERE m.queue IN ('SOLO', 'FLEX')
            AND p.champ_pos_code = :champPosCode
			AND r.code = :runeCode
            GROUP BY r.selections
            ORDER BY count DESC
        """,
        nativeQuery = true
    )
    fun countAllRankedRuneSelectionsByChampPosCodeAndRuneCode(champPosCode: String, runeCode: String): List<CountAndWinsTable<Array<Int>>>;

    @Query(
        """
            SELECT i.item_id AS value, COUNT(*) AS count, COUNT(CASE WHEN p.win = true THEN 1 END) AS wins
            FROM public.performance_items i
            JOIN public.performances p ON i.performance_id = p.performance_id
            JOIN public.matches m ON p.match_id = m.match_id
            WHERE m.queue IN ('SOLO', 'FLEX')
            AND p.champ_pos_code = :champPosCode
            AND i.type = :type
            GROUP BY i.item_id
            ORDER BY count DESC
        """,
        nativeQuery = true
    )
    fun countAllRankedItemIdsByChampPosCode(champPosCode: String, type: String): List<CountAndWinsTable<Int>>;

    @Query(
        """
            SELECT p.lane_opponent_champion_name AS value, COUNT(*) AS count, COUNT(CASE WHEN p.win = true THEN 1 END) AS wins
            FROM public.performances p
            JOIN public.matches m ON p.match_id = m.match_id
            WHERE m.queue IN ('SOLO', 'FLEX')
            AND p.champ_pos_code = :champPosCode
            AND p.lane_opponent_champion_name != '[null]'
            GROUP BY p.lane_opponent_champion_name
            ORDER BY count DESC
        """,
        nativeQuery = true
    )
    fun countAllRankedMatchUpChampionNamesByChampPosCode(champPosCode: String): List<CountAndWinsTable<String>>;

    @Query(
        """
            SELECT AVG(p.kills) AS avg_kills,
            AVG(p.deaths) AS avg_deaths,
            AVG(p.assists) AS avg_assists,
            AVG(p.kda) AS avg_kda,
            AVG(p.kill_participation) AS avg_kp,
            AVG(p.gold_per_min) AS avg_gpm,
            AVG(p.cs_per_min) AS avg_cspm,
            AVG(p.damage_per_min) AS avg_dpm,
            AVG(p.physical_damage_dealt) AS avg_physical_dmg,
			AVG(p.magic_damage_dealt) AS avg_magic_dmg,
			AVG(p.true_damage_dealt) AS avg_true_dmg,
            AVG(p.turret_damage_per_min) AS avg_tdpm,
            AVG(p.penta_kills) AS avg_penta,
            AVG(p.solo_kills) AS avg_solokills,
            AVG(p.kb_score) AS avg_kbscore
            FROM public.performances p
            JOIN public.matches m ON p.match_id = m.match_id
            WHERE m.queue IN ('SOLO', 'FLEX')
            AND p.champ_pos_code = :champPosCode
        """,
        nativeQuery = true
    )
    fun calculateAvgStatsByChampPosCode(champPosCode: String): ChampionAvgStatsTable;

}