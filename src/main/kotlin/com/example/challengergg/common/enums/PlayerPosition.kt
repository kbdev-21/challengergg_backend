package com.example.challengergg.common.enums

enum class PlayerPosition(val riotPositionString: String, val order: Int) {
    TOP("TOP",0),
    JGL("JUNGLE",1),
    MID("MIDDLE",2 ),
    ADC("BOTTOM",3),
    SPT("UTILITY",4),
    UNK("", -1)
}