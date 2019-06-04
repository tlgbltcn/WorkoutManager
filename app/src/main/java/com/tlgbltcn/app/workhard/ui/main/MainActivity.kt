package com.tlgbltcn.app.workhard.ui.main

import android.content.*
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomappbar.BottomAppBar
import com.tlgbltcn.app.workhard.App
import com.tlgbltcn.app.workhard.R
import com.tlgbltcn.app.workhard.core.BaseActivity
import com.tlgbltcn.app.workhard.databinding.ActivityMainBinding
import com.tlgbltcn.app.workhard.db.AppDatabase
import com.tlgbltcn.app.workhard.db.entities.Stats
import com.tlgbltcn.app.workhard.ui.main.fragments.bottom.BottomNavigationDrawerFragment
import com.tlgbltcn.app.workhard.ui.main.fragments.bottom.ManageFragments
import com.tlgbltcn.app.workhard.ui.main.fragments.timer.TimerFragment
import com.tlgbltcn.app.workhard.ui.main.fragments.timer.TimerFragment.Companion.ARGUMENT_FRAGMENT
import com.tlgbltcn.app.workhard.utils.extensions.loadFragment
import com.tlgbltcn.app.workhard.utils.extensions.replaceFragmentInActivity
import com.tlgbltcn.app.workhard.utils.extensions.simpleToolbarWithHome
import com.tlgbltcn.app.workhard.utils.service.AppConstant
import com.tlgbltcn.app.workhard.utils.service.AppConstant.ABOUT_FRAGMENT
import com.tlgbltcn.app.workhard.utils.service.AppConstant.STATS_FRAGMENT
import com.tlgbltcn.app.workhard.utils.service.AppConstant.TIMER_FRAGMENT
import com.tlgbltcn.app.workhard.utils.service.TimerService
import javax.inject.Inject

class MainActivity : BaseActivity<MainActivityViewModel, ActivityMainBinding>(MainActivityViewModel::class.java), OnClickCallBack, ManageFragments {

    @Inject
    lateinit var pref: SharedPreferences
    @Inject
    lateinit var db: AppDatabase
    lateinit var stats: Stats
    private var isService = false
    private var isReceiverWork = false
    private lateinit var bottomNavigationDrawerFragment: BottomNavigationDrawerFragment
    private var sharedViewModel: SharedViewModel? = null
    private lateinit var updateUIReceiver: BroadcastReceiver
    private var maxValueWork = WORK_TIME
    private var maxValuePause = BREAK_TIME
    private var maxWork: Int = 0
    private var maxPause: Int = 0
    private var cycleCount: Int = 0
    private var whichFragment = TIMER_FRAGMENT

    override fun initViewModel(viewModel: MainActivityViewModel) {
        binding.viewModel = viewModel
        binding.handler = this
    }

    override fun getLayoutRes() = R.layout.activity_main


    override fun changeFragment(tag: String) {
        when (tag) {
            STATS_FRAGMENT -> {
                whichFragment = STATS_FRAGMENT
                binding.appbar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fabBar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_36dp))
            }
            ABOUT_FRAGMENT -> {
                whichFragment = ABOUT_FRAGMENT
                binding.appbar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fabBar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_36dp))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).component.inject(this)
        bottomNavigationDrawerFragment = BottomNavigationDrawerFragment()
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        simpleToolbarWithHome(binding.appbar)
        bottomNavigationDrawerFragment.mFragmentManager = this
        window.setBackgroundDrawableResource(R.color.windowBackground)
        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)
        observeInput(sharedViewModel!!)
        initDb()
        initMinutes()
        initFragment()
    }

    private fun initDb() {
        db.statsDao().getCycleCount(0).observe(this, Observer {
            if (it == null) viewModel.createTableToDb()
        })
    }

    private fun initMinutes() {
        maxValueWork = prepareTimerValueInt(pref.getInt(getString(R.string.max_work), 2))
        maxValuePause = prepareTimerValueInt(pref.getInt(getString(R.string.max_pause), 1))
    }

    private fun observeInput(sharedViewModel: SharedViewModel) {
        sharedViewModel.getNewTimeSet().observe(this, Observer {
            if (it && isReceiverWork) {
                stopTimerService()
                fabNavigate(false)
            }
        })
    }

    private fun initFragment() {
        replaceFragmentInActivity(timerFragment(), R.id.container)
        whichFragment = TIMER_FRAGMENT
    }

    private fun timerFragment() = supportFragmentManager.findFragmentById(R.id.container)
            ?: TimerFragment.newInstance().apply {
                arguments = Bundle().apply {
                    putString(ARGUMENT_FRAGMENT, intent.getStringExtra(ARGUMENT_FRAGMENT))
                }
            }


    fun getVisibleFragment(): Fragment? {
        val fragmentManager = this@MainActivity.supportFragmentManager
        val fragments = fragmentManager.fragments
        if (fragments != null) {
            for (fragment in fragments) {
                if (fragment != null && fragment.isVisible)
                    return fragment
            }
        }
        return null
    }


    override fun onClickFab() {

        when (whichFragment) {
            STATS_FRAGMENT -> {
                loadFragment(TimerFragment())
                whichFragment = TIMER_FRAGMENT
                fabNavigate(isService)

            }
            ABOUT_FRAGMENT -> {
                loadFragment(TimerFragment())
                whichFragment = TIMER_FRAGMENT
                fabNavigate(isService)

            }
            TIMER_FRAGMENT -> {
                if (binding.appbar.fabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_END) {
                    fabNavigate(false)
                    stopTimerService()
                    pref.edit().putBoolean("isFabCenter", false).apply()
                    sharedViewModel?.isClick?.postValue(false)

                } else {
                    fabNavigate(true)
                    startTimer()
                }

            }
        }

    }

    fun fabNavigate(isService: Boolean) {
        this.isService = isService
        if (isService) {
            binding.appbar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
            binding.fabBar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_close_black_24dp))
        } else {
            binding.appbar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
            binding.fabBar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_hourglass_empty_black_36dp))
        }

    }

    fun prepareTimerValueLong(time: Long) = ((time / 1000) / 60).toInt()

    fun prepareTimerValueInt(time: Int) = ((time * 1000) * 60).toLong()

    private fun startTimer() {
        initMinutes()
        pref.edit().putBoolean("isFabCenter", true).apply()
        initReceiver()
        val intent = Intent(this, TimerService::class.java)
        sharedViewModel?.apply {
            remainigTimeMax.postValue(prepareTimerValueLong(maxValueWork))
            isClick.postValue(true)
        }
        intent.putExtra(getString(R.string.work), maxValueWork)
        startService(intent)

    }

    private fun stopTimerService() {
        stopService(Intent(this, TimerService::class.java))
        isReceiverWork = false
        unregisterReceiver(updateUIReceiver)
        sharedViewModel?.apply {
            remainigTimeMax.postValue(prepareTimerValueLong(maxValueWork))
            remaininTime.postValue(prepareTimerValueLong(maxValueWork).toString())
            remaininTimeProgress.postValue(prepareTimerValueLong(maxValueWork))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                bottomNavigationDrawerFragment.show(supportFragmentManager, "NavigationFragment")
            }
        }
        return true
    }


    override fun onPause() {
        if (bottomNavigationDrawerFragment.isAdded) bottomNavigationDrawerFragment.dismiss()
        super.onPause()
    }


    private fun initReceiver() {
        isReceiverWork = true
        val intentFilter = IntentFilter()
        intentFilter.addAction(AppConstant.SERVICE_TAG)
        intentFilter.addAction(getString(R.string.work))
        intentFilter.addAction(getString(R.string.work_finish))
        intentFilter.addAction(getString(R.string.pause))
        intentFilter.addAction(getString(R.string.pause_finish))
        updateUIReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

                val workData = intent?.getIntExtra(getString(R.string.work), -1)
                val breakData = intent?.getIntExtra(getString(R.string.pause), -1)
                val timerData: Int
                val getWorkingTimeInfo: String
                val getBreakingTimeInfo: String

                timerData = if (workData!! > breakData!!) workData else breakData
                getWorkingTimeInfo = if (workData > breakData) getString(R.string.work) else getString(R.string.pause)
                getBreakingTimeInfo = if (workData > breakData) getString(R.string.work_finish) else getString(R.string.pause_finish)

                when (intent.action) {
                    getWorkingTimeInfo -> {
                        sharedViewModel?.remaininTime?.postValue(timerData.toString())
                        sharedViewModel?.remaininTimeProgress?.value = timerData
                    }
                    getBreakingTimeInfo -> {
                        recursiveServiceStart(getBreakingTimeInfo)
                        //sharedViewModel?.remaininTime?.value = dataBreak.toString()
                        //sharedViewModel?.remaininTimeProgress?.value = dataBreak
                    }
                }
            }
        }
        this.registerReceiver(updateUIReceiver, intentFilter)
    }

    private fun recursiveServiceStart(type: String) {
        val intent = Intent(this, TimerService::class.java)
        when (type) {
            getString(R.string.work_finish) -> {
                stopService(Intent(this, TimerService::class.java))
                sharedViewModel?.remainigTimeMax?.postValue(prepareTimerValueLong(maxValuePause))
                sharedViewModel?.isWork?.postValue(false)
                intent.putExtra(getString(R.string.pause), maxValuePause)
                viewModel.increaseWorkCycleCount()
            }
            getString(R.string.pause_finish) -> {
                stopService(Intent(this, TimerService::class.java))
                sharedViewModel?.remainigTimeMax?.postValue(prepareTimerValueLong(maxValueWork))
                sharedViewModel?.isWork?.postValue(true)
                intent.putExtra(getString(R.string.work), maxValueWork)
            }
        }
        this.startService(intent)
    }

    companion object {
        const val WORK_TIME = 120000L
        const val BREAK_TIME = 60000L
    }
}
