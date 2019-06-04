package com.tlgbltcn.app.workhard.ui.main.fragments.bottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tlgbltcn.app.workhard.R
import com.tlgbltcn.app.workhard.ui.main.fragments.about.AboutFragment
import com.tlgbltcn.app.workhard.ui.main.fragments.stats.StatsFragment
import com.tlgbltcn.app.workhard.utils.extensions.loadFragment
import com.tlgbltcn.app.workhard.utils.extensions.toast
import kotlinx.android.synthetic.main.bottom_navigationview.*

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    val STATS_FRAGMENT = "StatsFragment"
    val ABOUT_FRAGMENT = "AboutFragment"
    lateinit var mFragmentManager : ManageFragments


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.bottom_navigationview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
           fragmentManager
        navigation_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.app_bar_setting -> {
                    mFragmentManager.changeFragment(STATS_FRAGMENT)
                    loadFragment(StatsFragment.newInstance())
                    dismiss()
                }
                R.id.app_bar_about -> {
                    mFragmentManager.changeFragment(ABOUT_FRAGMENT)
                    loadFragment(AboutFragment.newInstance())
                    dismiss()
                }
                R.id.app_bar_stats -> {
                    toast("Stats", Toast.LENGTH_SHORT)
                }

            }
            true
        }
    }
}