package com.example.challengergg.common.util

import com.example.challengergg.enums.ItemType
import java.text.SimpleDateFormat
import java.util.*

class AppUtil {
    fun getItemType(itemId: Int): ItemType {
        return when (itemId) {
            0 -> ItemType.EMPTY;
            in ItemType.START.riotIdsList -> ItemType.START;
            in ItemType.BASE.riotIdsList -> ItemType.BASE;
            in ItemType.BOOT.riotIdsList -> ItemType.BOOT;
            in ItemType.UTILITY.riotIdsList -> ItemType.UTILITY;
            else -> ItemType.LEGENDARY;
        }
    }

    fun printLnWithTagAndDate(tag: String, content: String) {
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val currentDate = formatter.format(Date())

        val blue = "\u001B[34m"      // ANSI code draw blue
        val reset = "\u001B[0m"      // ANSI code reset color

        println("[$currentDate] ${blue}${tag.uppercase()}$reset $content")
    }
}