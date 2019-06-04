package com.tlgbltcn.app.workhard.ui.main.fragments.timer

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tlgbltcn.app.workhard.App
import com.tlgbltcn.app.workhard.R
import com.tlgbltcn.app.workhard.core.BaseFragment
import com.tlgbltcn.app.workhard.databinding.FragmentTimerBinding
import com.tlgbltcn.app.workhard.ui.main.SharedViewModel
import kotlinx.android.synthetic.main.dialog_set_time.view.*
import javax.inject.Inject

class TimerFragment : BaseFragment<TimerFragmentViewModel, FragmentTimerBinding>(TimerFragmentViewModel::class.java), TimerFragmentCallBack {

    @Inject
    lateinit var pref: SharedPreferences

    lateinit var dialogInterface : DialogInterface.OnClickListener

    lateinit var sharedViewModel: SharedViewModel

    var workTime : Int = 0
    var pauseTime : Int = 0

    override fun getLayoutRes(): Int = R.layout.fragment_timer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val result = super.onCreateView(inflater, container, savedInstanceState)
        (activity?.application as App).component.inject(this)
        pref = PreferenceManager.getDefaultSharedPreferences(activity)
        mBinding.viewModel = viewModel
        mBinding.handler = this
        initTimes()
        initializeUI()
        activity?.let {
            sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
            observeInput(sharedViewModel)
        }

        return result
    }

    private fun initTimes() {
        workTime = pref.getInt(MAX_WORK, DEFAULT_WORK)
        pauseTime = pref.getInt(MAX_PAUSE, DEFAULT_PAUSE)
    }

    private fun observeInput(sharedViewModel: SharedViewModel) {

        with(sharedViewModel) {
            isOnClick().observe(this@TimerFragment, Observer {
                if (it) startTimer() else stopTimer()
            })

            getRemainingTimeMax().observe(this@TimerFragment, Observer {
                mBinding.viewModel?.remaininTimeMax?.set(it)
            })

            getRemainingTime().observe(this@TimerFragment, Observer {
                mBinding.viewModel?.remainingTime?.set(it)
            })

            getRemainingTimeProgress().observe(this@TimerFragment, Observer {
                mBinding.viewModel?.remainingTimeProgress?.set(it)
            })

            getIsWork().observe(this@TimerFragment, Observer {
                mBinding.viewModel?.isWork?.set(it)
                if (it) mBinding.circularProgress.externalColor = ContextCompat.getColor(view?.context!!, R.color.windowBackground)
                else mBinding.circularProgress.externalColor = ContextCompat.getColor(view?.context!!, R.color.colorAccent)
            })
        }

    }

    override fun setTime() {

        val layout = layoutInflater.inflate(R.layout.dialog_set_time,null)
        layout.number_of_time_period_pause.text = pauseTime.toString()
        layout.number_of_time_period.text = workTime.toString()

        layout.minus_work.setOnClickListener {
            workTime--
            layout.number_of_time_period.text = workTime.toString()
        }
        layout.plus_work.setOnClickListener {
            workTime++
            layout.number_of_time_period.text = workTime.toString()
        }
        layout.minus_pause.setOnClickListener {
            pauseTime--
            layout.number_of_time_period_pause.text = pauseTime.toString()
        }
        layout.plus_pause.setOnClickListener {
            pauseTime++
            layout.number_of_time_period_pause.text = pauseTime.toString()
        }
        val alertDialog = AlertDialog.Builder(activity)
                .setView(layout)
                .setPositiveButton(getString(R.string.set),dialogInterface)
                .setNegativeButton(getString(R.string.cancel),dialogInterface)
                .show()

    }

    private fun startTimer() {
        //mBinding.viewModel!!.initializedText(40)
    }


    private fun stopTimer() {

    }

    private fun initializeUI() {
        mBinding.viewModel!!.initializedText(workTime)
        dialogInterface = DialogInterface.OnClickListener { dialogInterface, i ->
            when(i){
                DialogInterface.BUTTON_POSITIVE -> {
                    sharedViewModel.isNewTimeSet.postValue(true)
                    pref.edit().putInt(MAX_WORK, workTime).apply()
                    pref.edit().putInt(MAX_PAUSE, pauseTime).apply()
                    mBinding.viewModel?.remainingTime?.set(workTime.toString())
                    mBinding.viewModel?.remaininTimeMax?.set(workTime)
                    mBinding.viewModel?.remainingTimeProgress?.set(workTime)
                }
                DialogInterface.BUTTON_NEGATIVE -> {Toast.makeText(activity,"Negative",Toast.LENGTH_SHORT).show()}
            }

        }
    }


    companion object {
        const val ARGUMENT_FRAGMENT = "TIMER"
        const val MAX_WORK = "maxWork"
        const val MAX_PAUSE = "maxPause"
        const val DEFAULT_WORK = 52
        const val DEFAULT_PAUSE = 52
        fun newInstance() = TimerFragment()
    }
}