package com.tlgbltcn.app.workhard.ui.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tlgbltcn.app.workhard.App
import com.tlgbltcn.app.workhard.core.BaseViewModel
import com.tlgbltcn.app.workhard.db.AppDatabase
import com.tlgbltcn.app.workhard.db.entities.Stats
import com.tlgbltcn.app.workhard.utils.service.AppConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainActivityViewModel(app: Application) : BaseViewModel(app) {

    var fabOnFragment = AppConstant.TIMER_FRAGMENT
    var time = 40
    var remainingTime: MutableLiveData<String> = MutableLiveData()
    var remainingTimeProgress: MutableLiveData<Int> = MutableLiveData()
    var remaininTimeMax: MutableLiveData<Int> = MutableLiveData()
    var cycleCountLiveData: MutableLiveData<Int> = MutableLiveData()

    @Inject
    lateinit var db: AppDatabase

    init {
        (getApplication<Application>() as App).component.inject(this)
    }

    fun createTableToDb() {
        viewModelScope.launch(Dispatchers.IO) {
            db.statsDao().updateStats(Stats(0, 0))
        }
    }


    fun increaseWorkCycleCount() {
        runBlocking {
            var cycleCount = async(Dispatchers.IO) {
                db.statsDao().isAnyData(0)
            }.await()


            viewModelScope.launch(Dispatchers.IO) {
                db.statsDao().updateStats(Stats(0, cycleCount + 1))
            }
        }
    }
}