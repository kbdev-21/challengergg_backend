package com.example.challengergg.entity.query

interface ChampionAvgStatsTable {
    fun getAvgKills(): Double
    fun getAvgDeaths(): Double
    fun getAvgAssists(): Double
    fun getAvpKda(): Double
    fun getAvgKp(): Double
    fun getAvgGpm(): Double
    fun getAvgCspm(): Double
    fun getAvgDpm(): Double
    fun getAvgTdpm(): Double
    fun getAvgPenta(): Double
    fun getAvgKbscore(): Double
}