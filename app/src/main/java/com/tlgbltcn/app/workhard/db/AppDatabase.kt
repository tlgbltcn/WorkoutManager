package com.tlgbltcn.app.workhard.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tlgbltcn.app.workhard.db.dao.StatsDao
import com.tlgbltcn.app.workhard.db.entities.Stats

@Database(entities = [Stats::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun statsDao(): StatsDao
}