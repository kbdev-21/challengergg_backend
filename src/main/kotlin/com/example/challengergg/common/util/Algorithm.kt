package com.example.challengergg.common.util

import com.example.challengergg.common.enums.*
import com.example.challengergg.entity.Performance

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
        val maxKp = 0.8;
        val winWeight = 10.0;
        val dp10mWeight = 25.0;
        val goldDiffWeight = 10.0;
        val maxGoldDiff = 4000;

        var defaultScore = defaultWeight;

        var kpScore = (kp/maxKp) * kpWeight;
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
        var pickScore = pickRate*100;
        if(pickScore > 10) pickScore = 10.0;
        if(pickScore < 0) pickScore = 0.0;

        var winScore = winRate*100 - 45;
        if(winScore > 10) winScore = 10.0;
        if(winScore < 0) winScore = 0.0;

        val powerScore = (winScore/10)*65 + (pickScore/10)*35;
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
        if(tier == RankTier.UNK) return -1;

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