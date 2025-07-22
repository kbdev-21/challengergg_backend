package com.example.challengergg.entity.query

interface ChampionAvgStatsTable {
    fun getAvgKills(): Double
    fun getAvgDeaths(): Double
    fun getAvgAssists(): Double
    fun getAvgKda(): Double
    fun getAvgKp(): Double
    fun getAvgGpm(): Double
    fun getAvgCspm(): Double
    fun getAvgDpm(): Double
    fun getAvgPhysicalDmg(): Double
    fun getAvgMagicDmg(): Double
    fun getAvgTrueDmg(): Double
    fun getAvgTdpm(): Double
    fun getAvgPenta(): Double
    fun getAvgSolokills(): Double
    fun getAvgKbscore(): Double
}