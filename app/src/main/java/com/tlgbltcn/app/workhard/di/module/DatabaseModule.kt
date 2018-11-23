package com.tlgbltcn.app.workhard.di.module

import android.content.Context
import androidx.room.Room
import com.tlgbltcn.app.workhard.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun getDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context,
                AppDatabase::class.java, "example-db")
                .fallbackToDestructiveMigration()
                .build()
    }

}