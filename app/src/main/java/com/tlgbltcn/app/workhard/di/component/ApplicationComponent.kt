package com.tlgbltcn.app.workhard.di.component

import android.content.Context
import android.content.SharedPreferences
import com.tlgbltcn.app.workhard.App
import com.tlgbltcn.app.workhard.db.AppDatabase
import com.tlgbltcn.app.workhard.di.module.ApplicationModule
import com.tlgbltcn.app.workhard.di.module.DatabaseModule
import com.tlgbltcn.app.workhard.di.module.NetModule
import com.tlgbltcn.app.workhard.ui.main.MainActivity
import com.tlgbltcn.app.workhard.ui.main.MainActivityViewModel
import com.tlgbltcn.app.workhard.ui.main.fragments.about.AboutFragment
import com.tlgbltcn.app.workhard.ui.main.fragments.about.AboutFragmentViewModel
import com.tlgbltcn.app.workhard.ui.main.fragments.stats.StatsFragment
import com.tlgbltcn.app.workhard.ui.main.fragments.stats.StatsFragmentViewModel
import com.tlgbltcn.app.workhard.ui.main.fragments.timer.TimerFragment
import com.tlgbltcn.app.workhard.ui.main.fragments.timer.TimerFragmentViewModel
import dagger.Component
import javax.inject.Singleton


@Singleton

@Component(modules = [ApplicationModule::class, NetModule::class, DatabaseModule::class])


interface ApplicationComponent {
    fun app(): App

    fun context(): Context

    fun preferences(): SharedPreferences

    fun db() : AppDatabase

    fun inject(mainActivityViewModel: MainActivityViewModel)

    fun inject(mainActivity : MainActivity)

    fun inject(timerFragment : TimerFragment)

    fun inject(timerFragmentViewModel: TimerFragmentViewModel)

    fun inject(statsFragmentViewModel: StatsFragmentViewModel)

    fun inject(statsFragment: StatsFragment)

    fun inject(aboutFragmentViewModel: AboutFragmentViewModel)

    fun inject(aboutFragment: AboutFragment)
}
