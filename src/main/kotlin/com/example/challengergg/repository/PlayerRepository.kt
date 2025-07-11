package com.example.challengergg.repository

import com.example.challengergg.entity.Player
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PlayerRepository: JpaRepository<Player, UUID> {
    fun findByPuuid(puuid: String): Player?;
}