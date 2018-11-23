package com.tlgbltcn.app.workhard.ui.main.fragments.bottom

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tlgbltcn.app.workhard.R
import com.tlgbltcn.app.workhard.ui.main.fragments.about.AboutFragment
import com.tlgbltcn.app.workhard.ui.main.fragments.stats.StatsFragment
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
                    loadFragment(StatsFragment())
                    dismiss()
                }
                R.id.app_bar_about -> {
                    mFragmentManager.changeFragment(ABOUT_FRAGMENT)
                    loadFragment(AboutFragment())
                    dismiss()
                }
                R.id.app_bar_stats -> {
                    toast("Stats", Toast.LENGTH_SHORT)
                }

            }
            true
        }
    }


    fun loadFragment(fragment : Fragment?) {

        if(fragment != null){
            fragmentManager?.beginTransaction()
                    ?.replace(R.id.container, fragment)
                    ?.commit()
            }

    }
}