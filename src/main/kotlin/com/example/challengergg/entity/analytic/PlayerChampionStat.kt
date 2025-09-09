package com.example.challengergg.entity.analytic

import com.example.challengergg.enums.ChampTier
import com.example.challengergg.enums.PlayerPosition
import com.example.challengergg.enums.Region
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*

@Entity
@Table(name = "player_champion_stats")
class PlayerChampionStat {
    @Id
    var id: UUID = UUID.randomUUID();

    var puuid: String = "";

    var code: String = "";

    var createdAt: Date = Date();

    var championName: String = "";

    var championDisplayName: String = "";

    @Enumerated(EnumType.STRING)
    var position: PlayerPosition = PlayerPosition.UNK;

    var picks: Int = 0;

    var pickRate: Double = 0.0;

    var wins: Int = 0;

    var winRate: Double = 0.0;

    var avgKills: Double = 0.0;

    var avgDeaths: Double = 0.0;

    var avgAssists: Double = 0.0;

    var avgKda: Double = 0.0;

    var avgKp: Double = 0.0;

    var avgGpm: Double = 0.0;

    var avgCspm: Double = 0.0;

    var avgDpm: Double = 0.0;

    var avgPhysicalDmg: Double = 0.0;

    var avgMagicDmg: Double = 0.0;

    var avgTrueDmg: Double = 0.0;

    var avgTdpm: Double = 0.0;

    var avgPenta: Double = 0.0;

    var avgSolokills: Double = 0.0;

    var avgKbscore: Double = 0.0;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var matchUps: List<MatchUpStat> = listOf();
}