package com.example.challengergg.entity

import com.example.challengergg.enums.Region
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "players")
class Player {
    @Id
    @Column(name = "player_id")
    var id: UUID = UUID.randomUUID();

    @Column(unique = true, updatable = false)
    var puuid: String = "";

    var gameName: String = "";

    var tagLine: String = "";

    var searchName: String = "";

    var profileIconId: Int = 1;

    var summonerLevel: Long = 1;

    @Enumerated(EnumType.STRING)
    var region: Region = Region.VN;

    @OneToMany(mappedBy = "player", cascade = [(CascadeType.ALL)], orphanRemoval = true)
    var ranks: MutableList<PlayerRank> = mutableListOf();

    var createdAt: Date = Date();

    var updatedAt: Date = Date();
}