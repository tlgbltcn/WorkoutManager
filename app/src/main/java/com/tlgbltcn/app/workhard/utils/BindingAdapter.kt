package com.tlgbltcn.app.workhard.utils
import androidx.databinding.BindingAdapter

import com.github.glomadrian.dashedcircularprogress.DashedCircularProgress

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("app:set_circle_progress","app:set_max", requireAll = false)
    fun setCircleProgress(progressBar: DashedCircularProgress, percent: Int, max: Int) {
        progressBar.max = 40F
        progressBar.setValue(percent.toFloat())
    }

}