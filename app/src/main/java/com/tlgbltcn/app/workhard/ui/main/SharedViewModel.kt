package com.tlgbltcn.app.workhard.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    var isClick = MutableLiveData<Boolean>()
    var remainigTimeMax: MutableLiveData<Int> = MutableLiveData()
    var remaininTimeProgress: MutableLiveData<Int> = MutableLiveData()
    var remaininTime: MutableLiveData<String> = MutableLiveData()
    var isWork : MutableLiveData<Boolean> = MutableLiveData()
    var isNewTimeSet : MutableLiveData<Boolean> = MutableLiveData()

    fun getRemainingTimeProgress() = remaininTimeProgress
    fun getRemainingTime() = remaininTime
    fun getRemainingTimeMax() = remainigTimeMax
    fun getIsWork() = isWork
    fun getNewTimeSet() = isNewTimeSet
    fun isOnClick() = isClick

    init {
    }

}