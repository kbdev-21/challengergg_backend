package com.example.challengergg.external.dto

data class RiotMatchDto(
    var metadata: MetadataDto,
    var info: InfoDto,
)

data class MetadataDto(
    var matchId: String,
)

data class InfoDto(
    var endOfGameResult: String,
    var gameDuration: Long,
    var gameMode: String,
    var queueId: Int,
    var gameStartTimestamp: Long,
    var gameVersion: String,
    var participants: List<ParticipantDto>,
    var teams: List<TeamDto>
)

data class TeamDto(
    var objectives: ObjectivesDto,
    var teamId: Int,
    var win: Boolean,
)

data class ObjectivesDto(
    var baron: ObjectiveDto,
    var champion: ObjectiveDto,
    var dragon:	ObjectiveDto,
    var horde: ObjectiveDto,
    var inhibitor: ObjectiveDto,
    var riftHerald: ObjectiveDto,
    var tower: ObjectiveDto,
)

data class ObjectiveDto(
    var kills: Int
)

data class ParticipantDto(
    var assists: Int,
    var champLevel: Int,
    var championName: String,
    var damageDealtToBuildings: Int,
    var deaths: Int,
    var detectorWardsPlaced: Int,
    var doubleKills: Int,
    var dragonKills: Int,
    var firstBloodKill: Boolean,
    var gameEndedInEarlySurrender: Boolean,
    var gameEndedInSurrender: Boolean,
    var goldEarned: Int,
    var goldSpent: Int,
    var individualPosition: String,
    var item0: Int,
    var item1: Int,
    var item2: Int,
    var item3: Int,
    var item4: Int,
    var item5: Int,
    var item6: Int,
    var killingSprees: Int,
    var kills: Int,
    var lane: String,
    var largestKillingSpree: Int,
    var largestMultiKill: Int,
    var magicDamageDealtToChampions: Int,
    var magicDamageTaken: Int,
    var neutralMinionsKilled: Int,
    var objectivesStolen: Int,
    var pentaKills: Int,
    var physicalDamageDealtToChampions: Int,
    var physicalDamageTaken: Int,
    var puuid: String,
    var quadraKills: Int,
    var riotIdGameName: String,
    var riotIdTagline: String,
    var sightWardsBoughtInGame: Int,
    var spell1Casts: Int,
    var spell2Casts: Int,
    var spell3Casts: Int,
    var spell4Casts: Int,
    var summoner1Casts: Int,
    var summoner1Id: Int,
    var summoner2Casts: Int,
    var summoner2Id: Int,
    var teamId: Int,
    var teamPosition: String,
    var timeCCingOthers: Int,
    var totalAllyJungleMinionsKilled: Int,
    var totalDamageDealtToChampions: Int,
    var totalDamageShieldedOnTeammates: Int,
    var totalDamageTaken: Int,
    var totalEnemyJungleMinionsKilled: Int,
    var totalHeal: Int,
    var totalHealsOnTeammates: Int,
    var totalMinionsKilled: Int,
    var totalTimeCCDealt: Int,
    var totalTimeSpentDead: Int,
    var tripleKills: Int,
    var trueDamageDealtToChampions: Int,
    var trueDamageTaken: Int,
    var turretKills: Int,
    var turretTakedowns: Int,
    var visionScore: Int,
    var visionWardsBoughtInGame: Int,
    var wardsKilled: Int,
    var wardsPlaced: Int,
    var win: Boolean,
    var challenges: ChallengesDto?,
    var perks: PerksDto,
)

data class ChallengesDto(
    var earlyLaningPhaseGoldExpAdvantage: Int,
    var soloKills: Int,
    var kda: Float,
    var killParticipation: Float,
)

data class PerksDto(
    var styles: List<PerkStyleDto>
)

data class PerkStyleDto(
    var description: String,
    var style: Int,
    var selections: List<PerkStyleSelectionDto>
)

data class PerkStyleSelectionDto(
    var perk: Int,
    var var1: Int,
    var var2: Int,
    var var3: Int,
)