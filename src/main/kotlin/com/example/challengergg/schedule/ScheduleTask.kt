package com.example.challengergg.schedule

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
    @Scheduled(cron = "0 0 */1 * * *")
    fun updateChampionStats() {
        val start = System.currentTimeMillis();

        analyticService.updateChampionStats();

        val end = System.currentTimeMillis();
        val elapsedSeconds = (end - start) / 1000.0;
        println("SCHEDULE: Finished update champion stats in ${elapsedSeconds}s");
    }


    //@Scheduled(cron = "*/40 * * * * *")
    @Scheduled(cron = "0 */1 * * * *")
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
        var randomRank = when (randomTier) {
            RankTier.CHALLENGER -> (0..299).random();
            RankTier.GRANDMASTER -> (0..699).random();
            RankTier.MASTER -> (0..999).random();
            else -> -1;
        };
        val regions = Region.entries;

        regions.forEach { region ->
            if(region == Region.EUN) {
                randomRank = when (randomTier) {
                    RankTier.CHALLENGER -> (0..199).random();
                    RankTier.GRANDMASTER -> (0..499).random();
                    RankTier.MASTER -> (0..999).random();
                    else -> -1;
                };
            }

            val puuid = riotApi.getSoloDuoLeague(randomTier, region)?.entries?.get(randomRank)?.puuid
                ?: throw CustomException(HttpStatus.NOT_FOUND, "Cannot found puuid");

            val matchPerFetch = 10;

            matchService.getMatchesByPuuid(puuid, 420, 0, matchPerFetch, region);

            val end = System.currentTimeMillis();
            val elapsedSeconds = (end - start) / 1000.0;
            println("SCHEDULE: Finished fetch $matchPerFetch $puuid (${randomTier.toString()}) matches in ${elapsedSeconds}s (${region.toString()} server)");
        }

//        val puuid = riotApi.getSoloDuoLeague(randomTier, randomRegion)?.entries?.get(randomRank)?.puuid
//            ?: throw CustomException(HttpStatus.NOT_FOUND, "Cannot found puuid");
//
//        matchService.getMatchesByPuuid(puuid, 420, 0, 10, randomRegion);
//
//        val end = System.currentTimeMillis();
//        val elapsedSeconds = (end - start) / 1000.0;
//        println("SCHEDULE: Finished fetch $puuid (${randomTier.toString()}) matches in ${elapsedSeconds}s");
    }
}