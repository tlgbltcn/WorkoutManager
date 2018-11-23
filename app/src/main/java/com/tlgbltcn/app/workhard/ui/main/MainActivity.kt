package com.tlgbltcn.app.workhard.ui.main

import android.content.*
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomappbar.BottomAppBar
import com.tlgbltcn.app.workhard.App
import com.tlgbltcn.app.workhard.R
import com.tlgbltcn.app.workhard.core.BaseActivity
import com.tlgbltcn.app.workhard.databinding.ActivityMainBinding
import com.tlgbltcn.app.workhard.ui.main.fragments.bottom.BottomNavigationDrawerFragment
import com.tlgbltcn.app.workhard.ui.main.fragments.timer.TimerFragment
import com.tlgbltcn.app.workhard.utils.extensions.replaceFragmentInActivity
import com.tlgbltcn.app.workhard.utils.extensions.simpleToolbarWithHome
import com.tlgbltcn.app.workhard.utils.service.TimerService
import javax.inject.Inject
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tlgbltcn.app.workhard.db.AppDatabase
import com.tlgbltcn.app.workhard.db.entities.Stats
import com.tlgbltcn.app.workhard.ui.main.fragments.bottom.ManageFragments
import com.tlgbltcn.app.workhard.utils.extensions.loadFragment
import com.tlgbltcn.app.workhard.utils.service.AppConstant
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.jetbrains.anko.coroutines.experimental.bg


class MainActivity : BaseActivity<MainActivityViewModel, ActivityMainBinding>(MainActivityViewModel::class.java), OnClickCallBack, ManageFragments {

    @Inject
    lateinit var pref: SharedPreferences
    @Inject
    lateinit var db: AppDatabase
    var isAnyData : Int = 0
    lateinit var stats: Stats
    var isService = false
    var isReceiver = false
    private lateinit var bottomNavigationDrawerFragment: BottomNavigationDrawerFragment
    private var sharedViewModel: SharedViewModel? = null
    private lateinit var updateUIReceiver: BroadcastReceiver
    var maxValueWork = 2400000L
    var maxValuePause = 600000L
    var maxWork : Int = 0
    var maxPause : Int = 0
    var fabOnFragment = AppConstant.TIMER_FRAGMENT
    override fun initViewModel(viewModel: MainActivityViewModel) {
        binding.viewModel = viewModel
        binding.handler = this
    }

    override fun getLayoutRes() = R.layout.activity_main


    override fun changeFragment(tag: String) {
        when (tag) {
            AppConstant.STATS_FRAGMENT -> {
                fabOnFragment = AppConstant.STATS_FRAGMENT
                binding.appbar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fabBar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_36dp))
            }
            AppConstant.ABOUT_FRAGMENT -> {
                fabOnFragment = AppConstant.ABOUT_FRAGMENT
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
        async(UI){
            val data : Deferred<Any> = bg{
                isAnyData =  db.exampleDao().getCount()}
            data.await().let {
                if(isAnyData == 0) stats = Stats(0,0) }}

        bg { db.statsDao().updateStats(stats) }
    }

    private fun initMinutes() {
        maxValueWork = prepareTimerValueInt(pref.getInt(getString(R.string.max_work), 40))
        maxValuePause = prepareTimerValueInt(pref.getInt(getString(R.string.max_pause),20))
    }

    private fun observeInput(sharedViewModel: SharedViewModel) {
        sharedViewModel.getNewTimeSet().observe(this, Observer {
            if (it == true && isReceiver) {
                stopTimerService()
                fabNavigate(false)
            }
        })
    }

    private fun initFragment() {
        replaceFragmentInActivity(timerFragment(), R.id.container)
        fabOnFragment = AppConstant.TIMER_FRAGMENT
    }

    private fun timerFragment() = supportFragmentManager.findFragmentById(R.id.container)
            ?: TimerFragment.newInstance().apply {
                arguments = Bundle().apply {
                    putString(TimerFragment.ARGUMENT_FRAGMENT, intent.getStringExtra(TimerFragment.ARGUMENT_FRAGMENT))
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

        when (fabOnFragment) {
            AppConstant.STATS_FRAGMENT -> {
                loadFragment(TimerFragment())
                fabOnFragment = AppConstant.TIMER_FRAGMENT
                fabNavigate(isService)

            }
            AppConstant.ABOUT_FRAGMENT -> {
                loadFragment(TimerFragment())
                fabOnFragment = AppConstant.TIMER_FRAGMENT
                fabNavigate(isService)

            }
            AppConstant.TIMER_FRAGMENT -> {
                if (binding.appbar.fabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_END) {
                    isService = false
                    fabNavigate(isService)
                    stopTimerService()
                    pref.edit().putBoolean("isFabCenter", false).apply()
                    sharedViewModel?.isClick?.postValue(false)

                } else {
                    isService = true
                    fabNavigate(isService)
                    startTimer()
                    sharedViewModel?.isClick?.postValue(true)
                }

            }
        }

    }

    fun fabNavigate(isService: Boolean) {
        if (isService) {
            binding.appbar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
            binding.fabBar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_close_black_24dp))
        } else {
            binding.appbar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
            binding.fabBar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_hourglass_empty_black_36dp))
        }

    }


    fun prepareTimerValueLong(time: Long): Int {

        return ((time / 1000) / 60).toInt()
    }

    fun prepareTimerValueInt(time : Int): Long {

        return ((time * 1000) * 60).toLong()
    }

    private fun startTimer() {
        initMinutes()
        pref.edit().putBoolean("isFabCenter", true).apply()
        initReceiver()
        val intent = Intent(this, TimerService::class.java)
        sharedViewModel?.remainigTimeMax?.postValue(prepareTimerValueLong(maxValueWork))
        intent.putExtra(getString(R.string.work), maxValueWork)
        this.startService(intent)


    }

    private fun stopTimerService() {
        stopService(Intent(this, TimerService::class.java))
        isReceiver = false
        unregisterReceiver(updateUIReceiver)
        sharedViewModel?.remainigTimeMax?.postValue(prepareTimerValueLong(maxValueWork))
        sharedViewModel?.remaininTime?.postValue(prepareTimerValueLong(maxValueWork).toString())
        sharedViewModel?.remaininTimeProgress?.postValue(prepareTimerValueLong(maxValueWork))


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
        super.onPause()
        if (bottomNavigationDrawerFragment.isAdded) bottomNavigationDrawerFragment.dismiss()
    }


    private fun initReceiver() {
        isReceiver = true
        val intentFilter = IntentFilter()
        intentFilter.addAction(AppConstant.SERVICE_TAG)
        intentFilter.addAction(getString(R.string.work))
        intentFilter.addAction(getString(R.string.work_finish))
        intentFilter.addAction(getString(R.string.pause))
        intentFilter.addAction(getString(R.string.pause_finish))
        updateUIReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

                val dataWork = intent?.getIntExtra(getString(R.string.work), -1)
                val dataBreak = intent?.getIntExtra(getString(R.string.pause), -1)
                var countDownTime: Int
                var sendExtraString: String
                var sendExtraStringFinish: String

                countDownTime = if (dataWork!! > dataBreak!!) dataWork else dataBreak
                sendExtraString = if (dataWork > dataBreak) getString(R.string.work) else getString(R.string.pause)
                sendExtraStringFinish = if (dataWork > dataBreak) getString(R.string.work_finish) else getString(R.string.pause_finish)

                when {
                    intent.action.equals(sendExtraString) -> {
                        sharedViewModel?.remaininTime?.postValue(countDownTime.toString())
                        sharedViewModel?.remaininTimeProgress?.value = countDownTime
                    }
                    intent.action.equals(sendExtraStringFinish) -> {
                        recursiveServiceStart(sendExtraStringFinish)
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
            }
            getString(R.string.pause_finish) -> {
                stopService(Intent(this, TimerService::class.java))
                sharedViewModel?.remainigTimeMax?.postValue(prepareTimerValueLong(maxValueWork))
                sharedViewModel?.isWork?.postValue(true)
                intent.putExtra(getString(R.string.work), maxValueWork)
                increaseCycle()

            }
        }
        this.startService(intent)


    }

    private fun increaseCycle() {
        stats.id = 0
        var cycleCount = 0
        runBlocking {
            bg { cycleCount =  db.statsDao().isAnyData(0) }.await()
            stats.cycle = cycleCount + 1
            bg { db.statsDao().updateStats(stats) }
        }
    }

}
