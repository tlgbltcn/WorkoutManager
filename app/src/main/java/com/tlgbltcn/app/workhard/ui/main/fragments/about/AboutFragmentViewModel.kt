package com.tlgbltcn.app.workhard.ui.main.fragments.about

import android.app.Application
import com.tlgbltcn.app.workhard.App
import com.tlgbltcn.app.workhard.core.BaseViewModel

class AboutFragmentViewModel(app : Application) : BaseViewModel(app) {

    init {
        (getApplication<Application>() as App).component.inject(this)
    }
}