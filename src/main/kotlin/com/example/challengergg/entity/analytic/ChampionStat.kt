package com.example.challengergg.entity.analytic

import com.example.challengergg.enums.ChampTier
import com.example.challengergg.enums.PlayerPosition
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "champion_stats")
class ChampionStat {
    @Id
    var code: String = "";

    var championName: String = "";

    var championDisplayName: String = "";

    @Enumerated(EnumType.STRING)
    var position: PlayerPosition = PlayerPosition.UNK;

    var picks: Int = 0;

    var pickRate: Double = 0.0;

    var wins: Int = 0;

    var winRate: Double = 0.0;

    var power: Int = 0;

    @Enumerated(EnumType.STRING)
    var tier: ChampTier = ChampTier.S;

    var avgKills: Double = 0.0;

    var avgDeaths: Double = 0.0;

    var avgAssists: Double = 0.0;

    var avgKda: Double = 0.0;

    var avgKp: Double = 0.0;

    var avgGpm: Double = 0.0;

    var avgCspm: Double = 0.0;

    var avgDpm: Double = 0.0;

    var avgPhysicalDmg: Double = 0.0;

    var avgMagicDmg: Double = 0.0

    var avgTrueDmg: Double = 0.0

    var avgTdpm: Double = 0.0;

    var avgPenta: Double = 0.0;

    var avgSolokills: Double = 0.0;

    var avgKbscore: Double = 0.0;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var bestSpellCombos: List<SpellComboStat> = listOf();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var bestRunes: List<RuneStat> = listOf();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var bestLegendaryItems: List<ItemStat> = listOf();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var bestBootItems: List<ItemStat> = listOf();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var matchUps: List<MatchUpStat> = listOf();
}

open class PickAndWinStat {
    var picks: Int = 0;
    var pickRate: Double = 0.0;
    var wins: Int = 0;
    var winRate: Double = 0.0;
}

class SpellComboStat: PickAndWinStat() {
    var spell1: Int = 0;
    var spell2: Int = 0;
}

class RuneStat: PickAndWinStat() {
    var main: Int = 0;
    var mainStyle: Int = 0;
    var subStyle: Int = 0;
    var selections: List<Int> = listOf();
}

class ItemStat: PickAndWinStat() {
    var itemId: Int = 0;
}

class MatchUpStat: PickAndWinStat() {
    var opponentChampionName: String = "";
    var opponentChampionDisplayName: String = "";
}