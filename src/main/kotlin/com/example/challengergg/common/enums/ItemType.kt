package com.example.challengergg.common.enums

enum class ItemType(val riotIdsList: List<Int>) {
    START(listOf(1056, 1054, 1055, 1082, 1083)),
    BASE(listOf(1001, 1004, 1006, 1027, 1028, 1029, 1033, 1036, 1037, 1038, 1042, 1043, 1052, 1053, 1058, 2031, 2055)),
    BOOT(listOf(3006, 3009, 3010, 3013, 3020, 3047, 3111, 3158)),
    UTILITY(listOf(3340, 3363, 3364, 3330)),
    LEGENDARY(listOf()),
    OTHER(listOf()),
    EMPTY(listOf());
}
