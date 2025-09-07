package com.example.challengergg.common.util

import com.example.challengergg.entity.Performance
import com.example.challengergg.enums.ChampTier
import com.example.challengergg.enums.PlayerPosition
import com.example.challengergg.enums.RankDivision
import com.example.challengergg.enums.RankTier

class Algorithm {
    fun calculateKbScore(performance: Performance, otherPerformances: List<Performance>, gameDuration: Long): Int {
        val laneOpponentPerformance = findLaneOpponent(performance, otherPerformances);
        val kp = performance.killParticipation.toDouble();
        val deaths = performance.deaths.toDouble();
        val win = performance.win;
        val gold = performance.totalGold.toDouble();
        val opponentGold = laneOpponentPerformance?.totalGold?.toDouble() ?: (gold/2);
        val goldDiff = gold - opponentGold;
        val position = performance.position;
        val gameDurationInMinutes = gameDuration.toDouble() / 60;
        val dp10m = (deaths/gameDurationInMinutes) * 10; //deaths per 10 minutes

        val defaultWeight = 5.0;
        val kpWeight = 50.0;
        val maxKp = 0.75;
        val winWeight = 15.0;
        val dp10mWeight = 20.0;
        val goldDiffWeight = 10.0;
        val maxGoldDiff = 4000;

        var defaultScore = defaultWeight;

        val balancedKp = when(position) {
            PlayerPosition.TOP -> kp + 0.15;
            PlayerPosition.MID -> kp + 0.1;
            PlayerPosition.ADC -> kp + 0.05;
            else -> kp;
        }
        var kpScore = (balancedKp/maxKp) * kpWeight;
        if(kpScore > kpWeight) kpScore = kpWeight;

        var winScore = if(win) winWeight else 0.0;

        var dp10mScore = ((11.665 - dp10m*3.33)/10) * dp10mWeight; //these magic numbers appeared when calculated it long time ago, can do nothing now
        if(dp10mScore > dp10mWeight) dp10mScore = dp10mWeight;
        if(dp10mScore < -dp10mWeight/2) dp10mScore = -dp10mWeight/2;

        var goldDiffScore = goldDiffWeight/2 + ((goldDiff/maxGoldDiff) * goldDiffWeight)/2;
        if(goldDiffScore > goldDiffWeight) goldDiffScore = goldDiffWeight;
        if(goldDiffScore < 0) goldDiffScore = 0.0;
        if(position == PlayerPosition.SPT) goldDiffScore = goldDiffWeight/2;

        val kbScore = defaultScore + kpScore + winScore + dp10mScore + goldDiffScore;

        return kbScore.toInt();
    }

    fun findLaneOpponent(performance: Performance, otherPerformances: List<Performance>): Performance? {
        val laneOpponentPerformance = otherPerformances.find {
            it.team != performance.team && it.position == performance.position
        };
        return laneOpponentPerformance;
    }

    fun calculateChampPower(pickRate: Double, winRate: Double): Int {
        val winWeight = 60;
        val pickWeight = 40;

        val maxWinRate = 0.54;
        val minWinRate = 0.46;
        val maxPickRate = 0.075;
        val minPickRate = 0.0;

        var winScoreRatio = (winRate - minWinRate) / (maxWinRate - minWinRate);
        if(winScoreRatio > 1) winScoreRatio = 1.0;
        if(winScoreRatio < 0) winScoreRatio = 0.0;

        var pickScoreRatio = (pickRate - minPickRate) / (maxPickRate - minPickRate);
        if(pickScoreRatio > 1) pickScoreRatio = 1.0;
        if(pickScoreRatio < 0) pickScoreRatio = 0.0;

        val powerScore = winScoreRatio*winWeight + pickScoreRatio*pickWeight;
        return powerScore.toInt();
    }

    fun getChampTierByPower(power: Int): ChampTier {
        return when {
            power >= 80 -> ChampTier.S
            power >= 60 -> ChampTier.A
            power >= 40 -> ChampTier.B
            power >= 20 -> ChampTier.C
            else -> ChampTier.D
        }
    }

    fun calculateRankPower(tier: RankTier, division: RankDivision, points: Int): Int {
        if(tier == RankTier.UNRANKED) return -1;

        val pointsEachDivision = 100;
        val divisionsEachTier = 4;
        var tierWeight = tier.weight;
        var divisionWeight = division.weight;
        if(tier.weight >= RankTier.MASTER.weight) {
            tierWeight = RankTier.MASTER.weight;
            divisionWeight = 0;
        }

        val tierPoints = tierWeight * divisionsEachTier * pointsEachDivision;
        val divisionPoints = divisionWeight * pointsEachDivision;

        return tierPoints + divisionPoints + points;
    }
}