package com.tlgbltcn.app.workhard.ui.main.fragments.timer

import android.app.Application
import androidx.databinding.ObservableField
import com.tlgbltcn.app.workhard.App
import com.tlgbltcn.app.workhard.core.BaseViewModel
import org.jetbrains.anko.doAsync

class TimerFragmentViewModel(app : Application) : BaseViewModel(app){

    val remainingTime : ObservableField<String> = ObservableField()
    var remaininTimeMax: ObservableField<Int> = ObservableField(0)
    var remainingTimeProgress: ObservableField<Int> = ObservableField(0)
    var isWork : ObservableField<Boolean> = ObservableField(true)


    init {
        (getApplication<Application>() as App).component.inject(this)
    }


    fun initializedText(maxTime : Int){
        doAsync {
            for(i in 0..maxTime){
                remainingTime.set(i.toString())
                Thread.sleep(25)
            }
        }
    }

    fun setTimeDialog(){




    }
}