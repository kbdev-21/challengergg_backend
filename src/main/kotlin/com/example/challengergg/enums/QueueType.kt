package com.example.challengergg.enums

enum class QueueType(val riotQueueId: Int = -1) {
    SOLO(420),
    FLEX(440),
    ARAM(450),
    NORMAL,
    SPEC,
    UNK
}