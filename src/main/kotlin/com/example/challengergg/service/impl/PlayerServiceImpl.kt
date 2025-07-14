package com.example.challengergg.service.impl

import com.example.challengergg.common.enums.PlayerPosition
import com.example.challengergg.common.enums.QueueType
import com.example.challengergg.common.enums.RankDivision
import com.example.challengergg.common.enums.RankTier
import com.example.challengergg.common.util.Algorithm
import com.example.challengergg.common.util.StringUtil
import com.example.challengergg.dto.PlayerDto
import com.example.challengergg.entity.Player
import com.example.challengergg.entity.Rank
import com.example.challengergg.exception.CustomException
import com.example.challengergg.service.PlayerService
import com.example.challengergg.external.RiotApi
import com.example.challengergg.external.dto.RiotAccountDto
import com.example.challengergg.external.dto.RiotLeagueEntryDto
import com.example.challengergg.external.dto.RiotSummonerDto
import com.example.challengergg.repository.PlayerRepository
import org.modelmapper.ModelMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class PlayerServiceImpl(
    private val playerRepository: PlayerRepository,
    private val riotApi: RiotApi,
): PlayerService {
    private val algorithm = Algorithm();
    private val stringUtil = StringUtil();
    private val modelMapper = ModelMapper();

    override fun getPlayerByGameNameAndTagLine(gameName: String, tagLine: String): PlayerDto {
        val riotAccountDto = riotApi.getAccountByNameAndTag(gameName, tagLine)
            ?: throw CustomException(HttpStatus.NOT_FOUND, "Account not found");
        val riotSummonerDto = riotApi.getSummonerByPuuid(riotAccountDto.puuid)
            ?: throw CustomException(HttpStatus.NOT_FOUND, "Summoner not found");
        val riotLeagueEntryDtos = riotApi.getLeagueEntriesByPuuid(riotAccountDto.puuid)
            ?: throw CustomException(HttpStatus.NOT_FOUND, "League entries not found");

        val player = getPlayerByRiotDtos(riotAccountDto, riotSummonerDto, riotLeagueEntryDtos);
        val savedPlayer = playerRepository.save(player);

        return modelMapper.map(savedPlayer, PlayerDto::class.java);
    }

    private fun getPlayerByRiotDtos(
        riotAccountDto: RiotAccountDto,
        riotSummonerDto: RiotSummonerDto,
        riotLeagueEntryDtos: List<RiotLeagueEntryDto>
    ): Player {
        if(riotAccountDto.puuid != riotSummonerDto.puuid
            || riotLeagueEntryDtos.any{it.puuid != riotAccountDto.puuid}) {
            throw CustomException(HttpStatus.CONFLICT, "Puuids are conflicted");
        }

        val player = Player();
        player.puuid = riotAccountDto.puuid;
        player.gameName = riotAccountDto.gameName;
        player.tagLine = riotAccountDto.tagLine;
        player.searchName = stringUtil.normalizeVietnamese(riotAccountDto.gameName + "#" + riotAccountDto.tagLine);
        player.profileIconId = riotSummonerDto.profileIconId;
        player.summonerLevel = riotSummonerDto.summonerLevel;

        /* if player existed -> assign unchangeable fields from the old object to the new one
        * if new -> create 2 rank objects (solo and flex) */
        val existedPlayer = playerRepository.findByPuuid(player.puuid);
        if(existedPlayer != null) {
            player.id = existedPlayer.id;
            player.puuid = existedPlayer.puuid;
            player.createdAt = existedPlayer.createdAt;
            player.ranks = existedPlayer.ranks;
        }
        else {
            val rankSolo = Rank();
            rankSolo.player = player;
            rankSolo.queue = QueueType.SOLO;
            val rankFlex = Rank();
            rankFlex.player = player;
            rankFlex.queue = QueueType.FLEX;
            player.ranks = mutableListOf(rankSolo, rankFlex);
        }

        val soloDto = riotLeagueEntryDtos.find { dto -> dto.queueType == "RANKED_SOLO_5x5" };
        val flexDto = riotLeagueEntryDtos.find { dto -> dto.queueType == "RANKED_FLEX_SR" };
        for(rank in player.ranks) {
            if(rank.queue == QueueType.SOLO && soloDto != null) {
                rank.tier = RankTier.valueOf(soloDto.tier);
                rank.division = RankDivision.valueOf(soloDto.rank);
                rank.points = soloDto.leaguePoints;
                rank.wins = soloDto.wins;
                rank.losses = soloDto.losses;
                rank.winRate = rank.wins.toDouble() / (rank.wins + rank.losses).toDouble();
                rank.power = algorithm.calculateRankPower(rank.tier, rank.division, rank.points);
            }
            if(rank.queue == QueueType.FLEX && flexDto != null) {
                rank.tier = RankTier.valueOf(flexDto.tier);
                rank.division = RankDivision.valueOf(flexDto.rank);
                rank.points = flexDto.leaguePoints;
                rank.wins = flexDto.wins;
                rank.losses = flexDto.losses;
                rank.winRate = rank.wins.toDouble() / (rank.wins + rank.losses).toDouble();
                rank.power = algorithm.calculateRankPower(rank.tier, rank.division, rank.points);
            }
        }
        player.ranks.sortByDescending { it.power };

        return player;
    }


}