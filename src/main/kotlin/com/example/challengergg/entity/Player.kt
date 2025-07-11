package com.example.challengergg.entity

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

    @OneToMany(mappedBy = "player", cascade = [(CascadeType.ALL)], orphanRemoval = true)
    var ranks: MutableList<Rank> = mutableListOf();

    var createdAt: Date = Date();

    var updatedAt: Date = Date();
}