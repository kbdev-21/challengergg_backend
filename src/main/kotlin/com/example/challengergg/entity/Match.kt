package com.example.challengergg.entity

import com.example.challengergg.enums.QueueType
import com.example.challengergg.enums.RankTier
import com.example.challengergg.enums.Region
import com.example.challengergg.enums.TeamCode
import jakarta.persistence.*
import org.hibernate.annotations.BatchSize
import java.util.*

@Entity
@Table(name = "matches")
class Match {
    @Id
    @Column(name = "match_id")
    var id: UUID = UUID.randomUUID();

    @Column(name = "riot_match_id", unique = true, updatable = false)
    var matchId: String = "";

    var version: String = "";

    @Enumerated(EnumType.STRING)
    var queue: QueueType = QueueType.UNK;

    @Enumerated(EnumType.STRING)
    var region: Region = Region.VN;

    @Enumerated(EnumType.STRING)
    var avgRank: RankTier? = null;

    var avgRankPower: Int? = null;

    var duration: Long = 0;

    var startTimeStamp: Long = 0;

    @OneToMany(mappedBy = "match", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @BatchSize(size = 20)
    var performances: MutableList<Performance> = mutableListOf();

}