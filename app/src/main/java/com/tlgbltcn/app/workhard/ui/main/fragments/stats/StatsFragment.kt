package com.tlgbltcn.app.workhard.ui.main.fragments.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tlgbltcn.app.workhard.App
import com.tlgbltcn.app.workhard.R
import com.tlgbltcn.app.workhard.core.BaseFragment
import com.tlgbltcn.app.workhard.databinding.FragmentStatsBinding
import com.tlgbltcn.app.workhard.db.AppDatabase
import com.tlgbltcn.app.workhard.model.ApiService
import com.tlgbltcn.app.workhard.model.Popular
import com.tlgbltcn.app.workhard.model.ResultsItem
import com.tlgbltcn.app.workhard.utils.service.AppConstant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class StatsFragment : BaseFragment<StatsFragmentViewModel, FragmentStatsBinding>(StatsFragmentViewModel::class.java){

    @Inject
    lateinit var db : AppDatabase

    override fun getLayoutRes(): Int = R.layout.fragment_stats

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val result = super.onCreateView(inflater, container, savedInstanceState)
        (activity?.application as App).component.inject(this)
        mBinding.viewModel = viewModel

        db.statsDao().getCycleCount(0).observe(this, Observer {
            it.let { viewModel.cycleCount.set(Integer.toString(it)) }
        })


        val mapSample : Map<String, Any> = mapOf("name" to "df", "name" to "dsdsdf")
        mapSample.withDefault { it }.forEach(::print)

        return result
    }


}