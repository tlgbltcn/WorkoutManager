package com.tlgbltcn.app.workhard.ui.main.fragments.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tlgbltcn.app.workhard.App
import com.tlgbltcn.app.workhard.R
import com.tlgbltcn.app.workhard.core.BaseFragment
import com.tlgbltcn.app.workhard.databinding.FragmentAboutBinding

class AboutFragment : BaseFragment<AboutFragmentViewModel, FragmentAboutBinding>(AboutFragmentViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.fragment_about

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val result=  super.onCreateView(inflater, container, savedInstanceState)
        (activity?.application as App).component.inject(this)
        mBinding.viewModel = viewModel

        return result
    }

    companion object {
        fun newInstance() = AboutFragment()
    }
}