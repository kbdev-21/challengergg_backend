package com.example.challengergg.entity

import com.example.challengergg.common.enums.RankDivision
import com.example.challengergg.common.enums.QueueType
import com.example.challengergg.common.enums.RankTier
import com.example.challengergg.common.util.Algorithm
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "ranks")
class Rank() {
    @Id
    @Column(name = "rank_id")
    var id: UUID = UUID.randomUUID();

    @Enumerated(EnumType.STRING)
    var queue: QueueType = QueueType.UNK;

    @Enumerated(EnumType.STRING)
    var tier: RankTier = RankTier.UNK;

    @Enumerated(EnumType.STRING)
    var division: RankDivision = RankDivision.I;

    var points: Int = 0;

    var wins: Int = 0;

    var losses: Int = 0;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    var player: Player = Player();

    var power: Int = 0;
}