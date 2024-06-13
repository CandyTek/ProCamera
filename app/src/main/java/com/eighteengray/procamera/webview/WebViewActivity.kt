package com.eighteengray.procamera.webview

import android.os.Bundle
import android.webkit.WebSettings
import com.eighteengray.procamera.R
import com.eighteengray.procamera.databinding.ActivityWebViewBinding
import com.supaur.baseactivity.baseactivity.BaseActivity

class WebViewActivity: BaseActivity() {

    private val binding by lazy { ActivityWebViewBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        var webSettings = binding.bridgeWebView.settings
        webSettings.javaScriptEnabled = true
        webSettings.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小
        webSettings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        webSettings.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.displayZoomControls = false //隐藏原生的缩放控件
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK //关闭webview中缓存
        webSettings.allowFileAccess = true //设置可以访问文件
        webSettings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        webSettings.loadsImagesAutomatically = true //支持自动加载图片
        webSettings.defaultTextEncodingName = "utf-8" //设置编码格式


        binding.bridgeWebView.loadUrl("file:///android_asset/demo.html")


    }

}
