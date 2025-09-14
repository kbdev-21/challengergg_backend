package com.example.challengergg.external

import com.example.challengergg.exception.CustomException
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient

class DdragonApi {
    private val webClient = WebClient.builder().build();

    fun getCurrentLeagueVersion(): String {
        val response = webClient.get()
            .uri("https://ddragon.leagueoflegends.com/api/versions.json")
            .retrieve()
            .bodyToMono(Array<String>::class.java)
            .block();

        val currentVersion = response?.firstOrNull() ?: throw CustomException(HttpStatus.BAD_GATEWAY, "Could not find league patch");
        return currentVersion.split(".").take(2).joinToString(".");
    }

    suspend fun getCurrentLeagueVersionAsync(): String {
        val response: Array<String>? = webClient.get()
            .uri("https://ddragon.leagueoflegends.com/api/versions.json")
            .retrieve()
            .bodyToMono(Array<String>::class.java)
            .awaitSingleOrNull()

        val currentVersion = response?.firstOrNull()
            ?: throw CustomException(HttpStatus.BAD_GATEWAY, "Could not find league patch")

        return currentVersion.split(".").take(2).joinToString(".")
    }
}