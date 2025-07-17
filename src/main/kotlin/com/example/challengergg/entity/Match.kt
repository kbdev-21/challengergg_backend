package com.example.challengergg.entity

import com.example.challengergg.common.enums.QueueType
import com.example.challengergg.common.enums.TeamCode
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

    var duration: Long = 0;

    var startTimeStamp: Long = 0;

    @OneToMany(mappedBy = "match", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @BatchSize(size = 20)
    var performances: MutableList<Performance> = mutableListOf()

}