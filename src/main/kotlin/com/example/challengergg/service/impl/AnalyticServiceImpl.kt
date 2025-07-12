package com.example.challengergg.service.impl

import com.example.challengergg.common.enums.ItemType
import com.example.challengergg.common.enums.PlayerPosition
import com.example.challengergg.common.util.Algorithm
import com.example.challengergg.common.util.StringUtil
import com.example.challengergg.entity.analytic.*
import com.example.challengergg.repository.ChampionStatRepository
import com.example.challengergg.repository.MatchRepository
import com.example.challengergg.repository.PerformanceRepository
import com.example.challengergg.service.AnalyticService
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class AnalyticServiceImpl(
    private val performanceRepository: PerformanceRepository,
    private val matchRepository: MatchRepository,
    private val championStatRepository: ChampionStatRepository,
): AnalyticService {
    private val algorithm = Algorithm();
    private val modelMapper = ModelMapper();
    private val stringUtil = StringUtil();

    override fun updateChampionStats() {
        val totalMatches = matchRepository.countRankedMatches();
        val allChampPosCodesCount = performanceRepository.countAllRankedChampPosCodes();

        val newChampionStats = mutableListOf<ChampionStat>();
        for(champPosCodeData in allChampPosCodesCount) {
            val picks = champPosCodeData.getCount();
            val wins = champPosCodeData.getWins();

            val pickRate = picks.toDouble() / totalMatches.toDouble();
            val winRate = wins.toDouble() / picks.toDouble();

            if(pickRate < 0.005) {
                continue;
            }

            val power = algorithm.calculateChampPower(pickRate, winRate);
            val tier = algorithm.getChampTierByPower(power);

            val allAvgStats = performanceRepository.calculateAvgStatsByChampPosCode(champPosCodeData.getValue());

            /* spells */
            val allSpellComboCodesCount = performanceRepository
                .countAllRankedSpellComboCodesByChampPosCode(champPosCodeData.getValue());

            val spellComboStats = mutableListOf<SpellComboStat>();
            allSpellComboCodesCount.take(5).forEach { count ->
                val spellComboStat = SpellComboStat();
                spellComboStat.spell1 = count.getValue().split("-")[0].toInt();
                spellComboStat.spell2 = count.getValue().split("-")[1].toInt();
                spellComboStat.picks = count.getCount();
                spellComboStat.pickRate = count.getCount().toDouble() / picks.toDouble();
                spellComboStat.wins = count.getWins();
                spellComboStat.winRate = count.getWins().toDouble() / count.getCount().toDouble();
                spellComboStats.add(spellComboStat);
            }

            /* runes */
            val allRuneCodesCount = performanceRepository
                .countAllRankedRuneCodesByChampPosCode(champPosCodeData.getValue());
            val bestSelections = HashMap<String, List<Int>>();
            for(runeCodeCount in allRuneCodesCount) {
                bestSelections[runeCodeCount.getValue()] = performanceRepository
                    .countAllRankedRuneSelectionsByChampPosCodeAndRuneCode(
                        champPosCodeData.getValue(),
                        runeCodeCount.getValue()
                    )[0].getValue().toList();
            }

            val runeStats = mutableListOf<RuneStat>();
            allRuneCodesCount.take(5).forEach { count ->
                val runeStat = RuneStat();
                runeStat.main = count.getValue().split("-")[0].toInt();
                runeStat.mainStyle = count.getValue().split("-")[1].toInt();
                runeStat.subStyle = count.getValue().split("-")[2].toInt();
                runeStat.selections = bestSelections[count.getValue()].orEmpty();
                runeStat.picks = count.getCount();
                runeStat.pickRate = count.getCount().toDouble() / picks.toDouble();
                runeStat.wins = count.getWins();
                runeStat.winRate = count.getWins().toDouble() / count.getCount().toDouble();
                runeStats.add(runeStat);
            }

            /* legendary items */
            val allLegendaryItemIdsCount = performanceRepository
                .countAllRankedItemIdsByChampPosCode(champPosCodeData.getValue(), ItemType.LEGENDARY.toString());

            val legendaryItemStats = mutableListOf<ItemStat>();
            allLegendaryItemIdsCount.take(10).forEach { count ->
                val itemStat = ItemStat();
                itemStat.itemId = count.getValue();
                itemStat.picks = count.getCount();
                itemStat.pickRate = count.getCount().toDouble() / picks.toDouble();
                itemStat.wins = count.getWins();
                itemStat.winRate = count.getWins().toDouble() / count.getCount().toDouble();
                legendaryItemStats.add(itemStat);
            }

            /* boot items */
            val allBootItemIdsCount = performanceRepository
                .countAllRankedItemIdsByChampPosCode(champPosCodeData.getValue(), ItemType.BOOT.toString());

            val bootItemStats = mutableListOf<ItemStat>();
            allBootItemIdsCount.take(5).forEach { count ->
                val itemStat = ItemStat();
                itemStat.itemId = count.getValue();
                itemStat.picks = count.getCount();
                itemStat.pickRate = count.getCount().toDouble() / picks.toDouble();
                itemStat.wins = count.getWins();
                itemStat.winRate = count.getWins().toDouble() / count.getCount().toDouble();
                bootItemStats.add(itemStat);
            }

            /* matchups */
            val allMatchUpsCount = performanceRepository
                .countAllRankedMatchUpChampionNamesByChampPosCode(champPosCodeData.getValue());

            val matchUpStats = mutableListOf<MatchUpStat>();
            allMatchUpsCount.forEach { count ->
                val matchUpStat = MatchUpStat();
                matchUpStat.opponentChampionName = count.getValue();
                matchUpStat.opponentChampionDisplayName = stringUtil.getChampionDisplayName(count.getValue());
                matchUpStat.picks = count.getCount();
                matchUpStat.pickRate = count.getCount().toDouble() / picks.toDouble();
                matchUpStat.wins = count.getWins();
                matchUpStat.winRate = count.getWins().toDouble() / count.getCount().toDouble();
                matchUpStats.add(matchUpStat);
            }

            /* save */
            val champStat = modelMapper.map(allAvgStats, ChampionStat::class.java);
            champStat.code = champPosCodeData.getValue();
            champStat.championName = champPosCodeData.getValue().split("-")[0];
            champStat.championDisplayName = stringUtil.getChampionDisplayName(champStat.championName);
            champStat.position = PlayerPosition.valueOf(champPosCodeData.getValue().split("-")[1]);
            champStat.picks = picks;
            champStat.pickRate = pickRate;
            champStat.wins = wins;
            champStat.winRate = winRate;
            champStat.power = power;
            champStat.tier = tier;
            champStat.bestSpellCombos = spellComboStats;
            champStat.bestRunes = runeStats;
            champStat.bestLegendaryItems = legendaryItemStats;
            champStat.bestBootItems = bootItemStats;
            champStat.matchUps = matchUpStats;

            newChampionStats.add(champStat);
        }
        championStatRepository.saveAll(newChampionStats);
    }
}