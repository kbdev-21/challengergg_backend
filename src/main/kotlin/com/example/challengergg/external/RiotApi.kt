package com.example.challengergg.external

import com.example.challengergg.enums.RankTier
import com.example.challengergg.enums.Region
import com.example.challengergg.exception.CustomException
import com.example.challengergg.external.dto.*
import io.netty.channel.ChannelOption
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.time.Duration

@Component
class RiotApi(
    @Value("\${riot.api.key}") private val apiKey: String,
) {
    private final val connectionProvider = ConnectionProvider.builder("riot-api-pool")
        .maxConnections(100)          // số kết nối tối đa
        .maxIdleTime(Duration.ofSeconds(30))  // kết nối idle tối đa
        .maxLifeTime(Duration.ofMinutes(5))   // thời gian sống tối đa
        .pendingAcquireTimeout(Duration.ofSeconds(5)) // chờ khi pool full
        .build()

    private final val httpClient = HttpClient.create(connectionProvider)
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
        .responseTimeout(Duration.ofSeconds(20))

    private final val webClient = WebClient.builder()
        .clientConnector(
            ReactorClientHttpConnector(
                httpClient
            )
        )
        .codecs { config ->
            config.defaultCodecs().maxInMemorySize(5 * 1024 * 1024)
        }
        .build();

    fun getAccountByNameAndTag(gameName: String, tagLine: String, region: Region): RiotAccountDto? {
        val cluster = region.riotClusterIdForAccount;

        val url =
            "https://$cluster.api.riotgames.com/riot/account/v1/accounts/by-riot-id/$gameName/$tagLine?api_key=$apiKey"
        return webClient.get()
            .uri(url)
            .retrieve()
            .onStatus({ it.isSameCodeAs(HttpStatus.NOT_FOUND) }) {
                Mono.error(CustomException(HttpStatus.NOT_FOUND, "Not found"));
            }
            .bodyToMono(RiotAccountDto::class.java)
            .block();
    }

    fun getSummonerByPuuid(puuid: String, region: Region): RiotSummonerDto? {
        val regionId = region.riotRegionId;

        val url = "https://$regionId.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/$puuid?api_key=$apiKey";
        return webClient.get()
            .uri(url)
            .retrieve()
            .onStatus({ it.isSameCodeAs(HttpStatus.NOT_FOUND) }) {
                Mono.error(CustomException(HttpStatus.NOT_FOUND, "Not found"));
            }
            .bodyToMono(RiotSummonerDto::class.java)
            .block();
    }

    fun getLeagueEntriesByPuuid(puuid: String, region: Region): List<RiotLeagueEntryDto>? {
        val regionId = region.riotRegionId;

        val url = "https://$regionId.api.riotgames.com/lol/league/v4/entries/by-puuid/$puuid?api_key=$apiKey";
        return webClient.get()
            .uri(url)
            .retrieve()
            .onStatus({ it.isSameCodeAs(HttpStatus.NOT_FOUND) }) {
                Mono.error(CustomException(HttpStatus.NOT_FOUND, "Not found"));
            }
            .bodyToMono(object : ParameterizedTypeReference<List<RiotLeagueEntryDto>>() {})
            .block();
    }

    suspend fun getMatchIdsByPuuid(puuid: String, queueId: Int?, start: Int, count: Int, region: Region): List<String>? {
        val cluster = region.riotClusterId;

        //solo 420, flex 440
        val realQueue = queueId ?: "";
        val url =
            "https://$cluster.api.riotgames.com/lol/match/v5/matches/by-puuid/$puuid/ids?queue=$realQueue&start=$start&count=$count&api_key=$apiKey";
        return webClient.get()
            .uri(url)
            .retrieve()
            .onStatus({ it.isSameCodeAs(HttpStatus.NOT_FOUND) }) {
                Mono.error(CustomException(HttpStatus.NOT_FOUND, "Not found"));
            }
            .bodyToMono(object : ParameterizedTypeReference<List<String>>() {})
            .awaitSingleOrNull();
    }

    suspend fun getMatchByMatchId(matchId: String, region: Region): RiotMatchDto? {
        val cluster = region.riotClusterId;

        val url = "https://$cluster.api.riotgames.com/lol/match/v5/matches/$matchId?api_key=$apiKey";
        return webClient.get()
            .uri(url)
            .retrieve()
            .onStatus({ it.isSameCodeAs(HttpStatus.NOT_FOUND) }) {
                Mono.error(CustomException(HttpStatus.NOT_FOUND, "Not found"));
            }
            .bodyToMono(RiotMatchDto::class.java)
            .awaitSingleOrNull();
    }

    suspend fun getSoloDuoLeague(tier: RankTier, region: Region): RiotLeagueListDto? {
        if (tier.weight < RankTier.MASTER.weight) return null;
        val reqTierString = when (tier) {
            RankTier.MASTER -> "masterleagues";
            RankTier.GRANDMASTER -> "grandmasterleagues";
            RankTier.CHALLENGER -> "challengerleagues";
            else -> return null;
        }

        val regionId = region.riotRegionId;

        val url = "https://$regionId.api.riotgames.com/lol/league/v4/$reqTierString/by-queue/RANKED_SOLO_5x5?api_key=$apiKey";
        return webClient.get()
            .uri(url)
            .retrieve()
            .onStatus({ it.isSameCodeAs(HttpStatus.NOT_FOUND) }) {
                Mono.error(CustomException(HttpStatus.NOT_FOUND, "Not found"));
            }
            .bodyToMono(RiotLeagueListDto::class.java)
            .awaitSingleOrNull();
    }
}