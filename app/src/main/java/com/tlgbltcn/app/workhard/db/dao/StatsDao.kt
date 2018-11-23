package com.tlgbltcn.app.workhard.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tlgbltcn.app.workhard.db.entities.Stats

@Dao
interface StatsDao {

    @Query("DELETE FROM Stats")
    fun deleteStats()

    @Query("SELECT cycle FROM Stats WHERE id = :id")
    fun getCycleCount(id : Long): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateStats(stats: Stats)

    @Query("SELECT cycle FROM Stats WHERE id = :id")
    fun isAnyData(id : Long) : Int

}