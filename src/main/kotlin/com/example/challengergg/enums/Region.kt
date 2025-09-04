package com.example.challengergg.enums

enum class Region(val riotRegionId: String, val riotClusterId: String, val riotClusterIdForAccount: String) {
    VN(riotRegionId = "vn2", riotClusterId = "sea", riotClusterIdForAccount = "asia"),
    KR(riotRegionId = "kr", riotClusterId = "asia", riotClusterIdForAccount = "asia"),
    NA(riotRegionId = "na1", riotClusterId = "americas", riotClusterIdForAccount = "americas"),
    EUW(riotRegionId = "euw1", riotClusterId = "europe", riotClusterIdForAccount = "europe"),
    EUN(riotRegionId = "eun1", riotClusterId = "europe", riotClusterIdForAccount = "europe")
}