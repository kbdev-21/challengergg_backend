package com.example.challengergg.service.impl

import com.example.challengergg.common.enums.ItemType
import com.example.challengergg.common.enums.PlayerPosition
import com.example.challengergg.common.enums.QueueType
import com.example.challengergg.common.enums.TeamCode
import com.example.challengergg.common.util.Algorithm
import com.example.challengergg.common.util.StringUtil
import com.example.challengergg.dto.MatchDto
import com.example.challengergg.entity.PerformanceItem
import com.example.challengergg.entity.Match
import com.example.challengergg.entity.Performance
import com.example.challengergg.exception.CustomException
import com.example.challengergg.external.RiotApi
import com.example.challengergg.external.dto.ParticipantDto
import com.example.challengergg.external.dto.RiotMatchDto
import com.example.challengergg.repository.MatchRepository
import com.example.challengergg.service.MatchService
import org.modelmapper.ModelMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class MatchServiceImpl(
    private val matchRepository: MatchRepository,
    private val riotApi: RiotApi,
): MatchService {
    private val algorithm = Algorithm();
    private val stringUtil = StringUtil();
    private val modelMapper = ModelMapper();

    override fun getMatchesByPuuid(puuid: String): List<MatchDto> {
        val playerMatches = getMatchesAndSaveNewOnesByPuuid(puuid);

        return playerMatches
            .sortedByDescending { it.startTimeStamp }
            .map { match -> modelMapper.map(match, MatchDto::class.java) };
    }

    fun getMatchesAndSaveNewOnesByPuuid(puuid: String): List<Match> {
        val matchIds = riotApi.getMatchIdsByPuuid(puuid, null, 0, 20)
            ?: throw CustomException(HttpStatus.NOT_FOUND, "Puuid not found");
        val existedMatches = getExistedMatchesByMatchIds(matchIds);
        val newRiotMatchDtos = getRiotMatchDtosByMatchIdsUnlessItExisted(matchIds, existedMatches);

        val newMatches = newRiotMatchDtos.map { dto ->
            getMatchByRiotMatchDto(dto);
        }.toList();
        val savedMatches = matchRepository.saveAll(newMatches);
        println("NOTE: Saved ${savedMatches.size} new matches"); // PRINT

        return existedMatches + savedMatches;
    }

    private fun getExistedMatchesByMatchIds(matchIds: List<String>): List<Match> {
        val existedMatches = mutableListOf<Match>();
        for(matchId in matchIds) {
            val existedMatch = matchRepository.findByMatchId(matchId);
            if(existedMatch != null) {
                existedMatches.add(existedMatch);
            }
        }
        return existedMatches;
    }

    private fun getRiotMatchDtosByMatchIdsUnlessItExisted(
        matchIds: List<String>,
        existedMatches: List<Match>
    ): List<RiotMatchDto> {
        val existedMatchIds = existedMatches.map { it.matchId };

        val riotMatchDtos = matchIds
            .filterNot { it in existedMatchIds }
            .map { matchId ->
                riotApi.getMatchByMatchId(matchId)
                    ?: throw CustomException(HttpStatus.NOT_FOUND, "Match not found")
            }

        return riotMatchDtos;
    }

    private fun getMatchByRiotMatchDto(riotMatchDto: RiotMatchDto): Match {
        val match = Match();
        match.matchId = riotMatchDto.metadata.matchId;
        match.queue = when (riotMatchDto.info.queueId) {
            QueueType.SOLO.riotQueueId -> QueueType.SOLO;
            QueueType.FLEX.riotQueueId -> QueueType.FLEX;
            QueueType.ARAM.riotQueueId -> QueueType.ARAM;
            else -> QueueType.NORMAL;
        }
        match.version = riotMatchDto.info.gameVersion.split(".").take(2).joinToString(".");
        match.duration = riotMatchDto.info.gameDuration;
        match.startTimeStamp = riotMatchDto.info.gameStartTimestamp;
        match.performances = getPerformancesByRiotParticipantDto(riotMatchDto.info.participants, match);

        return match;
    }

    /* deal with performances */
    private fun getPerformancesByRiotParticipantDto(riotParticipantDtos: List<ParticipantDto>, match: Match): MutableList<Performance> {
        val performances = mutableListOf<Performance>();
        for(dto in riotParticipantDtos) {
            val performance = Performance();
            performance.match = match;

            performance.puuid = dto.puuid;
            performance.gameName = dto.riotIdGameName;
            performance.tagLine = dto.riotIdTagline;
            performance.team = when (dto.teamId) {
                TeamCode.BLUE.riotTeamId -> TeamCode.BLUE;
                TeamCode.RED.riotTeamId -> TeamCode.RED;
                else -> TeamCode.UNK;
            }
            performance.win = dto.win;
            performance.position = when (dto.teamPosition) {
                PlayerPosition.TOP.riotPositionString -> PlayerPosition.TOP;
                PlayerPosition.JGL.riotPositionString -> PlayerPosition.JGL;
                PlayerPosition.MID.riotPositionString -> PlayerPosition.MID;
                PlayerPosition.ADC.riotPositionString -> PlayerPosition.ADC;
                PlayerPosition.SPT.riotPositionString -> PlayerPosition.SPT;
                else -> PlayerPosition.UNK;
            }
            performance.championName = dto.championName;
            performance.championDisplayName = stringUtil.getChampionDisplayName(dto.championName);
            performance.champPosCode = performance.championName + "-" + performance.position.toString();
            performance.championLevel = dto.champLevel;
            performance.spell1Id = dto.summoner1Id;
            performance.spell2Id = dto.summoner2Id;
            performance.spellComboCode = listOf<Int>(
                performance.spell1Id,
                performance.spell2Id
            ).sortedBy { it }.joinToString("-");
            performance.kills = dto.kills;
            performance.deaths = dto.deaths;
            performance.assists = dto.assists;
            performance.kda = dto.challenges.kda.toDouble();
            performance.killParticipation = dto.challenges.killParticipation.toDouble();
            val minutes = (match.duration/60).toInt();
            performance.totalCs = dto.totalMinionsKilled + dto.neutralMinionsKilled;
            performance.csPerMin = performance.totalCs.toDouble() / minutes;
            performance.totalGold = dto.goldEarned;
            performance.goldPerMin = performance.totalGold / minutes;
            performance.totalDamageDealt = dto.totalDamageDealtToChampions;
            performance.damagePerMin = performance.totalDamageDealt / minutes;
            performance.magicDamageDealt = dto.magicDamageDealtToChampions;
            performance.physicalDamageDealt = dto.physicalDamageDealtToChampions;
            performance.trueDamageDealt = dto.trueDamageDealtToChampions;
            performance.totalDamageTaken = dto.totalDamageTaken;
            performance.totalTurretDamageDealt = dto.damageDealtToBuildings;
            performance.turretDamagePerMin = performance.totalTurretDamageDealt / minutes;
            val riotDtoItems = listOf(dto.item0, dto.item1, dto.item2, dto.item3, dto.item4, dto.item5, dto.item6);
            performance.performanceItems = MutableList(riotDtoItems.size) {PerformanceItem()};
            for ((i, item) in riotDtoItems.withIndex()) {
                performance.performanceItems[i].performance = performance;
                performance.performanceItems[i].itemId = item;
                performance.performanceItems[i].slot = i;
                performance.performanceItems[i].type = when (item) {
                    0 -> ItemType.EMPTY;
                    in ItemType.START.riotIdsList -> ItemType.START;
                    in ItemType.BASE.riotIdsList -> ItemType.BASE;
                    in ItemType.BOOT.riotIdsList -> ItemType.BOOT;
                    in ItemType.UTILITY.riotIdsList -> ItemType.UTILITY;
                    else -> ItemType.LEGENDARY;
                }
            }
            performance.soloKills = dto.challenges.soloKills;
            performance.doubleKills = dto.doubleKills;
            performance.tripleKills = dto.tripleKills;
            performance.quadraKills = dto.quadraKills;
            performance.pentaKills = dto.pentaKills;
            performance.visionScore = dto.visionScore;
            performance.wardsKilled = dto.wardsKilled;
            performance.wardsPlaced = dto.wardsPlaced;
            performance.pinkWardsPlaced = dto.detectorWardsPlaced;

            /* rune part */
            performance.rune.performance = performance;
            performance.rune.main = dto.perks.styles[0].selections[0].perk;
            performance.rune.mainStyle = dto.perks.styles[0].style;
            performance.rune.subStyle = dto.perks.styles[1].style;
            performance.rune.selections = mutableListOf(
                dto.perks.styles[0].selections[0].perk,
                dto.perks.styles[0].selections[1].perk,
                dto.perks.styles[0].selections[2].perk,
                dto.perks.styles[0].selections[3].perk,
                dto.perks.styles[1].selections[0].perk,
                dto.perks.styles[1].selections[1].perk
            );
            performance.rune.code = performance.rune.main.toString() + "-" + performance.rune.mainStyle.toString() + "-" + performance.rune.subStyle.toString();

            performances.add(performance);
        }

        /* calculate opponent, KB score and MVP */
        var highestKbScore = 0;

        for(performance in performances) {
            performance.laneOpponentChampionName = algorithm.findLaneOpponent(performance, performances)?.championName;
            val kbScore = algorithm.calculateKbScore(performance, performances, match.duration);
            if(kbScore > highestKbScore) highestKbScore = kbScore;
            performance.kbScore = kbScore;
        }
        performances
            .filter { it.kbScore == highestKbScore }
            .sortedBy { it.deaths }
            .first().mvp = true;

        return performances;
    }
}