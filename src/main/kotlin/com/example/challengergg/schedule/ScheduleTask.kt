package com.example.challengergg.schedule

import com.example.challengergg.common.util.AppUtil
import com.example.challengergg.enums.RankTier
import com.example.challengergg.enums.Region
import com.example.challengergg.exception.CustomException
import com.example.challengergg.external.RiotApi
import com.example.challengergg.service.AnalyticService
import com.example.challengergg.service.MatchService
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduleTask(
    private val analyticService: AnalyticService,
    private val riotApi: RiotApi,
    private val matchService: MatchService
) {
    private val appUtil = AppUtil();

    @Scheduled(cron = "0 0 */1 * * *")
    fun updateChampionStats() {
        appUtil.printLnWithTagAndDate("schedule", "Start update champion stats...");
        val start = System.currentTimeMillis();

        analyticService.updateChampionStats();

        val end = System.currentTimeMillis();
        val elapsedSeconds = (end - start) / 1000.0;
        appUtil.printLnWithTagAndDate("schedule", "Finished update champion stats in ${elapsedSeconds}s");
    }

    @Scheduled(cron = "0 */2 * * * *")
    @Transactional
    suspend fun fetchMatches() {
        val start = System.currentTimeMillis();

        val eliteTiersWithRatios = listOf(
            RankTier.CHALLENGER,
            RankTier.CHALLENGER,
            RankTier.GRANDMASTER,
            RankTier.GRANDMASTER,
            RankTier.GRANDMASTER,
            RankTier.MASTER,
            RankTier.MASTER,
            RankTier.MASTER,
            RankTier.MASTER
        );
        val randomTier = eliteTiersWithRatios.random();
        val regions = Region.entries;

        regions.forEach { region ->
            val challengerBreakpoint = if(listOf(Region.EUN, Region.BR).contains(region)) 199 else 299;
            val grandmasterBreakpoint = if(listOf(Region.EUN, Region.BR).contains(region)) 499 else 699;
            val masterBreakpoint = 999;
            val randomRank = when (randomTier) {
                RankTier.CHALLENGER -> (0..challengerBreakpoint).random();
                RankTier.GRANDMASTER -> (0..grandmasterBreakpoint).random();
                RankTier.MASTER -> (0..masterBreakpoint).random();
                else -> -1;
            };

            val puuid = riotApi.getSoloDuoLeague(randomTier, region)?.entries?.get(randomRank)?.puuid
                ?: throw CustomException(HttpStatus.NOT_FOUND, "Cannot found puuid");

            val matchPerFetch = 10;

            matchService.getMatchesByPuuid(puuid, 420, 0, matchPerFetch, region);

            val end = System.currentTimeMillis();
            val elapsedSeconds = (end - start) / 1000.0;
            appUtil.printLnWithTagAndDate(
                "schedule",
                "Finished fetch $matchPerFetch ${region.toString()} ${randomTier.toString()} matches in ${elapsedSeconds}s ($puuid)"
            );
        }
    }
}