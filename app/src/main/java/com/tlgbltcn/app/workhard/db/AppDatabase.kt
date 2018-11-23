package com.tlgbltcn.app.workhard.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tlgbltcn.app.workhard.db.dao.ExampleDao
import com.tlgbltcn.app.workhard.db.dao.StatsDao
import com.tlgbltcn.app.workhard.db.entities.Example
import com.tlgbltcn.app.workhard.db.entities.Stats

@Database(entities = [Example::class, Stats::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exampleDao(): ExampleDao
    abstract fun statsDao(): StatsDao

}