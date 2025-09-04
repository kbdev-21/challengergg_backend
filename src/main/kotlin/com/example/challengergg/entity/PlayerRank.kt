package com.example.challengergg.entity

import com.example.challengergg.enums.RankDivision
import com.example.challengergg.enums.QueueType
import com.example.challengergg.enums.RankTier
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "ranks")
class PlayerRank() {
    @Id
    @Column(name = "rank_id")
    var id: UUID = UUID.randomUUID();

    @Enumerated(EnumType.STRING)
    var queue: QueueType = QueueType.UNK;

    @Enumerated(EnumType.STRING)
    var tier: RankTier = RankTier.UNRANKED;

    @Enumerated(EnumType.STRING)
    var division: RankDivision = RankDivision.I;

    var points: Int = 0;

    var wins: Int = 0;

    var losses: Int = 0;

    var winRate: Double = 0.0;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    var player: Player = Player();

    var power: Int = 0;
}