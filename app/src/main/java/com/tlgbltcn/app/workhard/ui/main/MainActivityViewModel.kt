package com.tlgbltcn.app.workhard.ui.main

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomappbar.BottomAppBar
import com.tlgbltcn.app.workhard.R
import com.tlgbltcn.app.workhard.App
import com.tlgbltcn.app.workhard.core.BaseViewModel
import com.tlgbltcn.app.workhard.db.AppDatabase
import com.tlgbltcn.app.workhard.ui.main.fragments.bottom.ManageFragments
import com.tlgbltcn.app.workhard.utils.service.AppConstant
import javax.inject.Inject

class MainActivityViewModel(app: Application) : BaseViewModel(app){


    var fabOnFragment = AppConstant.TIMER_FRAGMENT
    var time = 40
    var remainingTime: MutableLiveData<String> = MutableLiveData()
    var remainingTimeProgress: MutableLiveData<Int> = MutableLiveData()
    var remaininTimeMax: MutableLiveData<Int> = MutableLiveData()


    @Inject
    lateinit var db : AppDatabase

    init {
        (getApplication<Application>() as App).component.inject(this)
    }


}