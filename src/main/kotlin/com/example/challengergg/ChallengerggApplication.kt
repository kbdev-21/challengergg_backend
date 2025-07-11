package com.example.challengergg

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class ChallengerggApplication

fun main(args: Array<String>) {
	runApplication<ChallengerggApplication>(*args)
}
