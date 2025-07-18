package com.example.challengergg.external

import com.example.challengergg.common.enums.RankTier
import com.example.challengergg.exception.CustomException
import com.example.challengergg.external.dto.*
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class RiotApi(
    @Value("\${riot.api.key}") private val apiKey: String,
) {
    val webClient = WebClient.builder()
        .codecs { config ->
            config.defaultCodecs().maxInMemorySize(5 * 1024 * 1024)
        }
        .build();

    fun getAccountByNameAndTag(gameName: String, tagLine: String): RiotAccountDto? {
        val url = "https://asia.api.riotgames.com/riot/account/v1/accounts/by-riot-id/$gameName/$tagLine?api_key=$apiKey"
        return webClient.get()
            .uri(url)
            .retrieve()
            .onStatus({it.isSameCodeAs(HttpStatus.NOT_FOUND)}) {
                Mono.error(CustomException(HttpStatus.NOT_FOUND, "Not found"));
            }
            .bodyToMono(RiotAccountDto::class.java)
            .block();
    }

    fun getSummonerByPuuid(puuid: String): RiotSummonerDto? {
        val url = "https://vn2.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/$puuid?api_key=$apiKey";
        return webClient.get()
            .uri(url)
            .retrieve()
            .onStatus({it.isSameCodeAs(HttpStatus.NOT_FOUND)}) {
                Mono.error(CustomException(HttpStatus.NOT_FOUND, "Not found"));
            }
            .bodyToMono(RiotSummonerDto::class.java)
            .block();
    }

    fun getLeagueEntriesByPuuid(puuid: String): List<RiotLeagueEntryDto>? {
        val url = "https://vn2.api.riotgames.com/lol/league/v4/entries/by-puuid/$puuid?api_key=$apiKey";
        return webClient.get()
            .uri(url)
            .retrieve()
            .onStatus({it.isSameCodeAs(HttpStatus.NOT_FOUND)}) {
                Mono.error(CustomException(HttpStatus.NOT_FOUND, "Not found"));
            }
            .bodyToMono(object : ParameterizedTypeReference<List<RiotLeagueEntryDto>>() {})
            .block();
    }

    fun getMatchIdsByPuuid(puuid: String, queue: Int?, start: Int, count: Int): List<String>? {
        //solo 420, flex 440
        val realQueue = queue ?: "";
        val url = "https://sea.api.riotgames.com/lol/match/v5/matches/by-puuid/$puuid/ids?queue=$realQueue&start=${start}&count=$count&api_key=$apiKey";
        return webClient.get()
            .uri(url)
            .retrieve()
            .onStatus({it.isSameCodeAs(HttpStatus.NOT_FOUND)}) {
                Mono.error(CustomException(HttpStatus.NOT_FOUND, "Not found"));
            }
            .bodyToMono(object : ParameterizedTypeReference<List<String>>() {})
            .block();
    }

     suspend fun getMatchByMatchId(matchId: String): RiotMatchDto? {
        val url = "https://sea.api.riotgames.com/lol/match/v5/matches/$matchId?api_key=$apiKey";
        return webClient.get()
            .uri(url)
            .retrieve()
            .onStatus({it.isSameCodeAs(HttpStatus.NOT_FOUND)}) {
                Mono.error(CustomException(HttpStatus.NOT_FOUND, "Not found"));
            }
            .bodyToMono(RiotMatchDto::class.java)
            .awaitSingleOrNull();
    }

    fun getSoloDuoLeague(tier: RankTier): RiotLeagueListDto? {
        if(tier.weight < RankTier.MASTER.weight) return null;
        val reqTierString = when (tier) {
            RankTier.MASTER -> "masterleagues";
            RankTier.GRANDMASTER -> "grandmasterleagues";
            RankTier.CHALLENGER -> "challengerleagues";
            else -> return null;
        }
        val url = "https://vn2.api.riotgames.com/lol/league/v4/$reqTierString/by-queue/RANKED_SOLO_5x5?api_key=$apiKey";
        return webClient.get()
            .uri(url)
            .retrieve()
            .onStatus({it.isSameCodeAs(HttpStatus.NOT_FOUND)}) {
                Mono.error(CustomException(HttpStatus.NOT_FOUND, "Not found"));
            }
            .bodyToMono(RiotLeagueListDto::class.java)
            .block();
    }
}