package com.example.challengergg.entity

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*

@Entity
@Table(name = "runes")
class Rune {
    @Id
    @Column(name = "rune_id")
    var id: UUID = UUID.randomUUID();

    @OneToOne(mappedBy = "rune")
    var performance: Performance? = null;

    /* use to identify a rune set choice: {main}-{subStyle} */
    var code: String = "";

    var main: Int = 0;

    var mainStyle: Int = 0;

    var subStyle: Int = 0;

    @JdbcTypeCode(SqlTypes.ARRAY)
    var selections: MutableList<Int> = mutableListOf();

}