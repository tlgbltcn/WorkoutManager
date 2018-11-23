package com.tlgbltcn.app.workhard.ui.main.fragments.stats

import android.app.Application
import androidx.databinding.ObservableField
import com.tlgbltcn.app.workhard.App
import com.tlgbltcn.app.workhard.core.BaseViewModel

class StatsFragmentViewModel(app: Application) : BaseViewModel(app) {

    var cycleCount: ObservableField<String> = ObservableField(0.toString())


    init {
        (getApplication<Application>() as App).component.inject(this)
    }


}