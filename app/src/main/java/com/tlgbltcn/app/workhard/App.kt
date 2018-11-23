package com.tlgbltcn.app.workhard

import androidx.multidex.MultiDexApplication
import com.tlgbltcn.app.workhard.di.component.DaggerApplicationComponent
import com.tlgbltcn.app.workhard.di.module.ApplicationModule
import com.tlgbltcn.app.workhard.di.module.DatabaseModule
import com.tlgbltcn.app.workhard.di.module.NetModule

class App : MultiDexApplication() {

    val component by lazy {
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .databaseModule(DatabaseModule())
                .netModule(NetModule())
                .build()
    }
}

