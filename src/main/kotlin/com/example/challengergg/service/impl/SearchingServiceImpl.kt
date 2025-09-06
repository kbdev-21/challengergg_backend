package com.example.challengergg.service.impl

import com.example.challengergg.dto.ChampionStatSummaryDto
import com.example.challengergg.dto.PlayerDto
import com.example.challengergg.dto.SearchAllResultDto
import com.example.challengergg.repository.ChampionStatRepository
import com.example.challengergg.repository.PlayerRepository
import com.example.challengergg.service.SearchingService
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class SearchingServiceImpl(
    private val playerRepository: PlayerRepository,
    private val championStatRepository: ChampionStatRepository
): SearchingService {
    private val modelMapper = ModelMapper();

    override fun searchAll(key: String): SearchAllResultDto {
        val players = playerRepository.searchForSearchNameByKey(key);
        val champions = championStatRepository.searchForNameOrDisplayNameByKey(key);

        players.forEach { player ->
            player.ranks.sortByDescending { it.power }
        }

        val result = SearchAllResultDto();
        result.players = players.map { player -> modelMapper.map(player, PlayerDto::class.java) }
            .take(20);
        result.champions = champions.map { champ -> modelMapper.map(champ, ChampionStatSummaryDto::class.java) }
            .take(20);
        return result;
    }
}