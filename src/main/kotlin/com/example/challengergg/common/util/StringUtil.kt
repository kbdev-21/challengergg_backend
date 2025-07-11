package com.example.challengergg.common.util

import java.text.Normalizer

class StringUtil {
    fun normalizeVietnamese(text: String): String {
        var normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        normalized = normalized.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "");
        normalized = normalized.replace('đ', 'd').replace('Đ', 'd');
        return normalized.lowercase();
    }

    fun getChampionDisplayName(name: String): String {
        when (name) {
            "DrMundo" -> return "Dr. Mundo";
            "JarvanIV" -> return "Jarvan IV";
            "Kaisa" -> return "Kai'Sa";
            "Khazix" -> return "Kha'Zix";
            "KogMaw" -> return "Kog'Maw";
            "KSante" -> return "K'Sante";
            "LeeSin" -> return "Lee Sin";
            "MissFortune" -> return "Miss Fortune";
            "MonkeyKing" -> return "Wukong";
            "RekSai" -> return "Rek'Sai";
            "TahmKench" -> return "Tahm Kench";
            "TwistedFate" -> return "Twisted Fate";
            "AurelionSol" -> return "Aurelion Sol";
            "Chogath" -> return "Cho'Gath";
            "FiddleSticks" -> return "Fiddlesticks";
            "Leblanc" -> return "LeBlanc";
            "Reneta" -> return "Reneta Glasc";
            "Velkoz" -> return "Vel'Koz";
            "XinZhao" -> return "Xin Zhao";
            "Belveth" -> return "Bel'Veth"
            else -> return name;
        }
    }
}