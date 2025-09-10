package com.example.challengergg.service.impl

import com.example.challengergg.enums.ItemType
import com.example.challengergg.enums.PlayerPosition
import com.example.challengergg.common.util.Algorithm
import com.example.challengergg.common.util.AppUtil
import com.example.challengergg.common.util.StringUtil
import com.example.challengergg.dto.ChampionStatDetailDto
import com.example.challengergg.dto.PlayerChampionStatDto
import com.example.challengergg.entity.analytic.*
import com.example.challengergg.entity.query.CountAndWinsTable
import com.example.challengergg.exception.CustomException
import com.example.challengergg.external.DdragonApi
import com.example.challengergg.repository.ChampionStatRepository
import com.example.challengergg.repository.MatchRepository
import com.example.challengergg.repository.PerformanceRepository
import com.example.challengergg.repository.PlayerChampionStatRepository
import com.example.challengergg.service.AnalyticService
import org.modelmapper.ModelMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Date

@Service
class AnalyticServiceImpl(
    private val performanceRepository: PerformanceRepository,
    private val matchRepository: MatchRepository,
    private val championStatRepository: ChampionStatRepository,
    private val playerChampionStatRepository: PlayerChampionStatRepository
): AnalyticService {
    private val algorithm = Algorithm();
    private val modelMapper = ModelMapper();
    private val stringUtil = StringUtil();
    private val appUtil = AppUtil();

    override fun getAllChampionStats(): List<ChampionStatDetailDto> {
        return championStatRepository
            .findAll()
            .sortedByDescending { it.power }
            .map { stat ->
                modelMapper.map(stat, ChampionStatDetailDto::class.java);
            }
    }

    override fun getChampionStatsByChampionName(championName: String): List<ChampionStatDetailDto> {
        val championStats = championStatRepository.findByChampionName(championName)
            ?: throw CustomException(HttpStatus.NOT_FOUND, "Champion not found");
        return championStats
            .sortedByDescending { it.pickRate }
            .map { stat ->
                modelMapper.map(stat, ChampionStatDetailDto::class.java);
            }
    }

    @Transactional
    override fun updateChampionStats() {
        appUtil.printLnWithTagAndDate("update_stats", "Start updating champion stats...");
        val ddragonApi = DdragonApi();

        val currentVersion = ddragonApi.getCurrentLeagueVersion();

        val minimumMatches = 2000;
        val totalMatches = matchRepository.countRankedMatches(currentVersion);
        if(totalMatches < minimumMatches) {
            appUtil.printLnWithTagAndDate("update_stats", "Update cancelled: Too few matches for $currentVersion version.");
            throw CustomException(HttpStatus.UNPROCESSABLE_ENTITY, "Update cancelled: Too few matches for $currentVersion version.");
        };

        val allChampPosCodesCount = performanceRepository.countAllRankedChampPosCodes(currentVersion);

        val updateDate = Date();

        val newChampionStats = mutableListOf<ChampionStat>();
        for(champPosCodeData in allChampPosCodesCount) {
            val picks = champPosCodeData.getCount();
            val wins = champPosCodeData.getWins();

            val pickRate = picks.toDouble() / totalMatches.toDouble();
            val winRate = wins.toDouble() / picks.toDouble();

            val pickRateThreshold = 0.005;
            if(pickRate < pickRateThreshold) {
                continue;
            }

            val power = algorithm.calculateChampPower(pickRate, winRate);
            val tier = algorithm.getChampTierByPower(power);

            val allAvgStats = performanceRepository.calculateAvgStatsByChampPosCode(champPosCodeData.getValue(), currentVersion);

            /* spells */
            val allSpellComboCodesCount = performanceRepository
                .countAllRankedSpellComboCodesByChampPosCode(champPosCodeData.getValue(), currentVersion);

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
                .countAllRankedRuneCodesByChampPosCode(champPosCodeData.getValue(), currentVersion);
            val bestSelections = HashMap<String, List<Int>>();
            for(runeCodeCount in allRuneCodesCount) {
                bestSelections[runeCodeCount.getValue()] = performanceRepository
                    .countAllRankedRuneSelectionsByChampPosCodeAndRuneCode(
                        champPosCodeData.getValue(),
                        runeCodeCount.getValue(),
                        currentVersion
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

            /* items */
            val allItemIdsCount = performanceRepository
                .countAllRankedItemIdsByChampPosCode(champPosCodeData.getValue(), currentVersion);

            val legendaryItemStats = getItemStatsFromCountAndWinData(allItemIdsCount, ItemType.LEGENDARY, picks);

            val bootItemStats = getItemStatsFromCountAndWinData(allItemIdsCount, ItemType.BOOT, picks);

            /* matchups */
            val allMatchUpsCount = performanceRepository
                .countAllRankedMatchUpChampionNamesByChampPosCode(champPosCodeData.getValue(), currentVersion);

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
            champStat.createdAt = updateDate;
            champStat.version = currentVersion;

            newChampionStats.add(champStat);
        }
        championStatRepository.deleteAll();
        championStatRepository.saveAll(newChampionStats);
        appUtil.printLnWithTagAndDate("update_stats", "Finish update champion stats");
    }

    override fun getPlayerChampionStats(puuid: String): List<PlayerChampionStatDto> {
        return playerChampionStatRepository
            .findByPuuid(puuid)
            .sortedWith(
                compareByDescending<PlayerChampionStat> { it.picks }
                    .thenByDescending { it.wins }
                    .thenByDescending { it.avgKbscore }
            )
            .map { stat ->
                modelMapper.map(stat, PlayerChampionStatDto::class.java)
            };
    }

    override fun updatePlayerChampionStats(puuid: String) {
        val totalMatches = matchRepository.countPlayerRankedMatches(puuid);
        val allChampPosCodesCount = performanceRepository.countAllPlayerRankedChampPosCodes(puuid);

        val updateDate = Date();

        val newPlayerChampionStats = mutableListOf<PlayerChampionStat>();
        for(champPosCodeData in allChampPosCodesCount) {
            val picks = champPosCodeData.getCount();
            val wins = champPosCodeData.getWins();

            val pickRate = picks.toDouble() / totalMatches.toDouble();
            val winRate = wins.toDouble() / picks.toDouble();

            val pickRateThreshold = 0.005;
            if(pickRate < pickRateThreshold) {
                continue;
            }

            val allAvgStats = performanceRepository.calculatePlayerAvgStatsByChampPosCode(champPosCodeData.getValue(), puuid);

            /* matchups */
            val allMatchUpsCount = performanceRepository
                .countAllPlayerRankedMatchUpChampionNamesByChampPosCode(champPosCodeData.getValue(), puuid);

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
            val playerChampStat = modelMapper.map(allAvgStats, PlayerChampionStat::class.java);
            playerChampStat.puuid = puuid;
            playerChampStat.code = champPosCodeData.getValue();
            playerChampStat.championName = champPosCodeData.getValue().split("-")[0];
            playerChampStat.championDisplayName = stringUtil.getChampionDisplayName(playerChampStat.championName);
            playerChampStat.position = PlayerPosition.valueOf(champPosCodeData.getValue().split("-")[1]);
            playerChampStat.picks = picks;
            playerChampStat.pickRate = pickRate;
            playerChampStat.wins = wins;
            playerChampStat.winRate = winRate;
            playerChampStat.matchUps = matchUpStats;
            playerChampStat.createdAt = updateDate;

            newPlayerChampionStats.add(playerChampStat);
        }
        playerChampionStatRepository.deleteByPuuid(puuid);
        playerChampionStatRepository.saveAll(newPlayerChampionStats);
    }

    private fun getItemStatsFromCountAndWinData(
        itemIdsCount: List<CountAndWinsTable<Int>>,
        itemType: ItemType,
        picks: Int
    ): List<ItemStat> {
        val filteredItemStats = mutableListOf<ItemStat>();

        itemIdsCount
            .take(100)
            .filter { count -> itemType == appUtil.getItemType(count.getValue()) }
            .forEach { count ->
                val itemStat = ItemStat();
                itemStat.itemId = count.getValue();
                itemStat.picks = count.getCount();
                itemStat.pickRate = count.getCount().toDouble() / picks.toDouble();
                itemStat.wins = count.getWins();
                itemStat.winRate = count.getWins().toDouble() / count.getCount().toDouble();
                filteredItemStats.add(itemStat);
            }

        return filteredItemStats;
    }


}