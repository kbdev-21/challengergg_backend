package com.example.challengergg.mapper

import com.example.challengergg.common.util.StringUtil
import com.example.challengergg.dto.MatchDto
import com.example.challengergg.dto.PerformanceDto
import com.example.challengergg.entity.Match
import org.modelmapper.ModelMapper

class MatchMapper {
    private val modelMapper = ModelMapper();
    private val stringUtil = StringUtil();

    fun toMatchDto(match: Match): MatchDto {
        val matchDto = modelMapper.map(match, MatchDto::class.java);
        val performances = match.performances;
        val performanceDtos = mutableListOf<PerformanceDto>();
        for (performance in performances) {
            val performanceDto = modelMapper.map(performance, PerformanceDto::class.java);
            performanceDto.itemIds = performance.items.sortedBy{ it.slot }.map { item ->
                item.itemId;
            }.toMutableList();
            performanceDto.laneOpponentChampionDisplayName =
                performanceDto.laneOpponentChampionName?.let { stringUtil.getChampionDisplayName(it) };
            performanceDtos.add(performanceDto);
        }
        matchDto.performances = performanceDtos
            .sortedWith(compareBy({it.team.riotTeamId}, {it.position.order})).toMutableList();
        return matchDto;
    }
}