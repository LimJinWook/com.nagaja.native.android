package com.nagaja.app.android.WebView

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nagaja.app.android.Base.NagajaActivity
import com.nagaja.app.android.R
import com.nagaja.app.android.Utils.SharedPreferencesUtil


class WebViewActivity : NagajaActivity(), WebViewFragment.OnWebViewFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        selectLanguage(SharedPreferencesUtil().getSelectLanguage(this@WebViewActivity)!!, false)

        init()
    }

    private fun init() {
        val fragmentManager: FragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val tag: String = WebViewFragment::class.java.name
        val webViewFragment = WebViewFragment.newInstance()
        fragmentTransaction.add(R.id.activity_web_view_container, webViewFragment, tag).commit()
    }

    override fun onFinish() {
        finish()
    }

}