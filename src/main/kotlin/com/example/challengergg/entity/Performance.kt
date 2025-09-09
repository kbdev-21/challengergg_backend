package com.example.challengergg.entity

import com.example.challengergg.enums.PlayerPosition
import com.example.challengergg.enums.TeamCode
import jakarta.persistence.*
import org.hibernate.annotations.BatchSize
import java.util.UUID

@Entity
@Table(
    name = "performances",
    indexes = [
        Index(name = "idx_performance_puuid", columnList = "puuid")
    ]
)
class Performance() {
    @Id
    @Column(name = "performance_id")
    var id: UUID = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    lateinit var match: Match

    var puuid: String = "";

    var gameName: String = "";

    var tagLine: String = "";

    @Enumerated(EnumType.STRING)
    var team: TeamCode = TeamCode.BLUE;

    var win: Boolean = false;

    @Enumerated(EnumType.STRING)
    var position: PlayerPosition = PlayerPosition.UNK;

    var championName: String = "";

    var championDisplayName: String = "";

    /* {championName}-{position} */
    var champPosCode: String = "";

    var championLevel: Int = 0;

    var spell1Id: Int = 0;

    var spell2Id: Int = 0;

    /* {spell1Id}-{spell2Id} (sort asc) */
    var spellComboCode: String = "";

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "rune_id")
    var rune: PerformanceRune = PerformanceRune();

    var kills: Int = 0;

    var deaths: Int = 0;

    var assists: Int = 0;

    var kda: Double = 0.0;

    var killParticipation: Double = 0.0;

    var totalCs: Int = 0;

    var totalGold: Int = 0;

    var totalDamageDealt: Int = 0;

    var magicDamageDealt: Int = 0;

    var physicalDamageDealt: Int = 0;

    var trueDamageDealt: Int = 0;

    var totalDamageTaken: Int = 0;

    var totalTurretDamageDealt: Int = 0;

    @OneToMany(mappedBy = "performance", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @BatchSize(size = 200)
    var items: MutableList<PerformanceItem> = mutableListOf();

    var soloKills: Int = 0;

    var doubleKills: Int = 0;

    var tripleKills: Int = 0;

    var quadraKills: Int = 0;

    var pentaKills: Int = 0;

    var visionScore: Int = 0;

    var wardsKilled: Int = 0;

    var wardsPlaced: Int = 0;

    var pinkWardsPlaced: Int = 0;

    var kbScore: Int = 0;

    var kbScorePlacement: Int = 0;

    var mvp: Boolean = false;

    var svp: Boolean = false;

    var laneOpponentChampionName: String? = null;

    var goldPerMin: Int = 0;

    var csPerMin: Double = 0.0;

    var damagePerMin: Int = 0;

    var turretDamagePerMin: Int = 0;
}