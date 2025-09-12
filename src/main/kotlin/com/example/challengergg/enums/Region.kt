package com.example.challengergg.enums

enum class Region(val riotRegionId: String, val riotClusterId: String, val riotClusterIdForAccount: String) {
    VN(riotRegionId = "vn2", riotClusterId = "sea", riotClusterIdForAccount = "asia"),
    KR(riotRegionId = "kr", riotClusterId = "asia", riotClusterIdForAccount = "asia"),
    EUW(riotRegionId = "euw1", riotClusterId = "europe", riotClusterIdForAccount = "europe"),
    NA(riotRegionId = "na1", riotClusterId = "americas", riotClusterIdForAccount = "americas"),
    EUN(riotRegionId = "eun1", riotClusterId = "europe", riotClusterIdForAccount = "europe"),
    BR(riotRegionId = "br1", riotClusterId = "americas", riotClusterIdForAccount = "americas")
}