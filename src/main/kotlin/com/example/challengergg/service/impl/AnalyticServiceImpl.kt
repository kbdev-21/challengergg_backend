package com.example.challengergg.service.impl

import com.example.challengergg.common.enums.ItemType
import com.example.challengergg.common.util.Algorithm
import com.example.challengergg.repository.MatchRepository
import com.example.challengergg.repository.PerformanceRepository
import com.example.challengergg.service.AnalyticService
import org.springframework.stereotype.Service

@Service
class AnalyticServiceImpl(
    private val performanceRepository: PerformanceRepository,
    private val matchRepository: MatchRepository
): AnalyticService {
    private val algorithm = Algorithm();

    override fun updateChampionStats() {
        val totalMatches = matchRepository.countRankedMatches();
        val allChampPosCodesCount = performanceRepository.countAllRankedChampPosCodes();

        for(champPosCodeData in allChampPosCodesCount) {
            val picks = champPosCodeData.getCount();
            val wins = champPosCodeData.getWins();

            val pickRate = picks.toDouble() / totalMatches.toDouble();
            val winRate = wins.toDouble() / picks.toDouble();

            val power = algorithm.calculateChampPower(pickRate, winRate);
            val tier = algorithm.getChampTierByPower(power);

            val allAvgStats = performanceRepository.calculateAvgStatsByChampPosCode(champPosCodeData.getValue());

            val allSpellComboCodesCount = performanceRepository
                .countAllRankedSpellComboCodesByChampPosCode(champPosCodeData.getValue());

            val allRuneCodesCount = performanceRepository
                .countAllRankedRuneCodesByChampPosCode(champPosCodeData.getValue());
            val bestSelections = HashMap<String, List<Int>>();
            for(runeCodeCount in allRuneCodesCount) {
                bestSelections[runeCodeCount.getValue()] = performanceRepository
                    .countAllRankedRuneSelectionsByChampPosCodeAndRuneCode(
                        champPosCodeData.getValue(),
                        runeCodeCount.getValue()
                    )[0].getValue();
            }

            val allLegendaryItemIdsCount = performanceRepository
                .countAllRankedItemIdsByChampPosCode(champPosCodeData.getValue(), ItemType.LEGENDARY.toString());

            val allBootItemIdsCount = performanceRepository
                .countAllRankedItemIdsByChampPosCode(champPosCodeData.getValue(), ItemType.BOOT.toString());

            val allMatchUpsCount = performanceRepository
                .countAllRankedMatchUpChampionNamesByChampPosCode(champPosCodeData.getValue());
        }
    }
}