package com.tlgbltcn.app.workhard.core

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel


open class BaseViewModel(app: Application) : AndroidViewModel(app){

    open fun init(context: Context?) {}

}
