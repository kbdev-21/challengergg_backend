package com.example.challengergg.entity

import com.example.challengergg.enums.ItemType
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(
    name = "performance_items",
    indexes = [
        Index(name = "idx_performance_id", columnList = "performance_id"),
    ]
)
class PerformanceItem {
    @Id
    @Column(name = "performance_item_id")
    var id: UUID = UUID.randomUUID()

    @ManyToOne
    @JoinColumn(name = "performance_id", nullable = false)
    lateinit var performance: Performance

    var itemId: Int = 0

    var slot: Int = 0

    @Enumerated(EnumType.STRING)
    var type: ItemType = ItemType.OTHER
}
