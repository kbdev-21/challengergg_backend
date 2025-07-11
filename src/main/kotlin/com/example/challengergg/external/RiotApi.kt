package com.example.challengergg.external

import com.example.challengergg.common.enums.RankTier
import com.example.challengergg.exception.CustomException
import com.example.challengergg.external.dto.*
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class RiotApi {
    val webClient = WebClient.builder()
        .codecs { config ->
            config.defaultCodecs().maxInMemorySize(5 * 1024 * 1024)
        }
        .build();
    private val apiKey = "RGAPI-40e8ffd8-f8b9-4236-b8c1-de645d0e2516";

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

     fun getMatchByMatchId(matchId: String): RiotMatchDto? {
        val url = "https://sea.api.riotgames.com/lol/match/v5/matches/$matchId?api_key=$apiKey";
        return webClient.get()
            .uri(url)
            .retrieve()
            .onStatus({it.isSameCodeAs(HttpStatus.NOT_FOUND)}) {
                Mono.error(CustomException(HttpStatus.NOT_FOUND, "Not found"));
            }
            .bodyToMono(RiotMatchDto::class.java)
            .block();
    }

    fun getSoloDuoLeague(tier: RankTier): RiotLeagueListDto? {
        if(tier.weight < 7) return null;
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