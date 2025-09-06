package com.example.challengergg.common.util

import com.example.challengergg.enums.ItemType

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
}