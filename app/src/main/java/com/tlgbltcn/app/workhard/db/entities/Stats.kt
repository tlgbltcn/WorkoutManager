package com.tlgbltcn.app.workhard.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Stats")
data class Stats(
        @PrimaryKey
        @ColumnInfo(name = "id")
        var id: Long = 0,

        @ColumnInfo(name = "cycle")
        var cycle: Int
)