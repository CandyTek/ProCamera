package com.eighteengray.procamera.settings

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eighteengray.cardlibrary.bean.BaseDataBean
import com.eighteengray.procamera.R
import com.eighteengray.procamera.databinding.LayoutCommonTitleRecyclerBinding
import com.eighteengray.procameralibrary.common.Constants
import com.supaur.baseactivity.baseactivity.BaseActivity


class SettingsActivity : BaseActivity() {
    lateinit var settingsViewModel: SettingsViewModel
    private val binding by lazy { LayoutCommonTitleRecyclerBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        showProgressBar()

        settingsViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[SettingsViewModel::class.java]
        settingsViewModel?.updateViewState(this)
        settingsViewModel?.settingsViewState?.observe(this, Observer {
            hideProgressBar()
            when (it.showPageType) {
                ShowPageType.Error -> showErrorView()
                ShowPageType.Normal -> showData(it.settingsList)
                else -> {}
            }
        })

    }

    private fun showErrorView() {

    }

    private fun showData(settingsList: List<BaseDataBean<Settings>>?) {
        binding.rlSettings.showRecyclerView(settingsList, Constants.viewModelPackage)
    }


}
